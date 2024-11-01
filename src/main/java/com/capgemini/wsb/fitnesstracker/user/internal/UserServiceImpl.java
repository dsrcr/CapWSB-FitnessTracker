package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    /**
     * Creates a new user in the database.
     *
     * @param user the user object to be created
     * @return the saved User object
     * @throws IllegalArgumentException if the user already has a database ID
     */
    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    /**
     * Updates the information of an existing user.
     * <p>This method merges the details of the provided user entity with the
     * existing user record, applying only non-null fields.
     *
     * @param userId the ID of the user to update
     * @param user   the user entity containing updated information
     * @return the updated {@link User} entity after applying changes
     */
    @Override
    public User updateUser(Long userId, User user) {
        return userRepository.findById(userId).map(existingUser -> mergeUserUpdates(existingUser, user))
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    private User mergeUserUpdates(User existingUser, User newUser) {
        Optional.ofNullable(newUser.getFirstName()).ifPresent(existingUser::setFirstName);
        Optional.ofNullable(newUser.getLastName()).ifPresent(existingUser::setLastName);
        Optional.ofNullable(newUser.getBirthdate()).ifPresent(existingUser::setBirthdate);
        Optional.ofNullable(newUser.getEmail()).ifPresent(existingUser::setEmail);

        return userRepository.save(existingUser);
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId the ID of the user to retrieve
     * @return an Optional containing the User if found, or an empty Optional if not found
     */
    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user
     * @return an Optional containing the User if found, or an empty Optional if not found
     */
    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a list of all users in the database.
     *
     * @return a list of all users
     */
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their first and last name.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @return an Optional containing the User if found, or an empty Optional if not found
     */
    @Override
    public Optional<User> getUserByNameSurname(String firstName, String lastName) {
        return userRepository.getUserByNameSurname(firstName, lastName);
    }

    /**
     * Retrieves a list of users with a specific birthdate.
     *
     * @param birthdate the birthdate of the users in the format "yyyy-MM-dd"
     * @return a list of users who have the specified birthdate
     */
    @Override
    public List<User> getUserByBirthdate(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthdate) {
        return userRepository.getUserByBirthday(birthdate);
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if the user with the specified ID does not exist
     */
    @Override
    public void deleteUserById(Long id) {
        log.info("User is deleting {}", id);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Retrieves all users older than the specified date.
     *
     * @param date the date to compare against users' dates of birth
     * @return a list of users older than the specified date
     */
    @Override
    public List<User> findUsersOlderThan(LocalDate date) {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user.getBirthdate().isBefore(date))
                .collect(Collectors.toList());
    }
}