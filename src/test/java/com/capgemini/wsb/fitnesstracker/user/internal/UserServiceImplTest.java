package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateUser_shouldUpdateExistingUser() {
        User existingUser = new User("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        User updateUser = new User("John", "Smith", LocalDate.of(2001, 1, 1), "john.smith@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.updateUser(1L, updateUser);

        assertEquals("Smith", updatedUser.getLastName());
        assertEquals("john.smith@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_shouldThrowExceptionIfUserNotFound() {
        User updateUser = new User("John", "Smith", LocalDate.of(2001, 1, 1), "john.smith@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, updateUser));
        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUser_shouldReturnUser() {
        User user = new User("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void getUser_shouldReturnEmptyOptionalIfUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void deleteUserById_shouldDeleteUserIfExists() {
        User user = new User("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserById_shouldThrowExceptionIfUserNotExists() {
        when(userRepository.existsById(99L)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(99L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void findAllUsers_shouldReturnListOfUsers() {
        User user1 = new User("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        User user2 = new User("Jane", "Doe", LocalDate.of(2001, 2, 2), "jane.doe@example.com");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.findAllUsers();

        assertEquals(2, result.size());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    void findUsersOlderThan_shouldReturnUsersOlderThanDate() {
        User user1 = new User("John", "Doe", LocalDate.of(1999, 1, 1), "john.doe@example.com");
        User user2 = new User("Jane", "Doe", LocalDate.of(2002, 2, 2), "jane.doe@example.com");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.findUsersOlderThan(LocalDate.of(2000, 1, 1));

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void getUserByEmail_shouldReturnUserIfExists() {
        User user = new User("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("john.doe@example.com");

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void getUserByEmail_shouldReturnEmptyOptionalIfUserNotExists() {
        when(userRepository.findByEmail("non.existent@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("non.existent@example.com");

        assertFalse(result.isPresent());
    }
}
