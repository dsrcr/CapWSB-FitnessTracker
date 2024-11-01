package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    private MockMvc mockMvc;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        userDto = new UserDto(1L, "John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value(userDto.firstName()))
                .andExpect(jsonPath("$[0].lastName").value(userDto.lastName()));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        when(userService.getUser(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/v1/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(userDto.firstName()));
    }

    @Test
    void shouldThrowUserNotFoundException_whenUserNotFound() throws Exception {
        when(userService.getUser(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/users/{id}", 1L))
                .andExpect(status().isNotFound());
    }

//    @Test
//    void shouldAddUser() throws Exception {
//        when(userMapper.toEntity(userDto)).thenReturn(user);
//        when(userService.createUser(any(User.class))).thenReturn(user);
//
//        mockMvc.perform(post("/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(userDto)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void shouldUpdateUser() throws Exception {
//        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(user);
//
//        mockMvc.perform(put("/v1/users/{id}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(user)))
//                .andExpect(status().isOk());
//    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/v1/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnUsersOlderThan() throws Exception {
        when(userService.findUsersOlderThan(any(LocalDate.class))).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/v1/users/older/{date}", LocalDate.of(2024, 8, 10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(userDto.firstName()));
    }

    @Test
    void shouldReturnUserByEmail() throws Exception {
        when(userService.getUserByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/v1/users/email/{email}", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userDto.firstName()));
    }

    @Test
    void shouldReturnUsersByBirthdate() throws Exception {
        when(userService.getUserByBirthdate(any(LocalDate.class))).thenReturn(List.of(user));
        when(userMapper.toUserIdBirthdayDto(user)).thenReturn(new UserIdBirthdayInfo(user.getId(), user.getBirthdate()));

        mockMvc.perform(get("/v1/users/birthdate?birthday={birthday}", LocalDate.of(2000, 1, 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()));
    }
}
