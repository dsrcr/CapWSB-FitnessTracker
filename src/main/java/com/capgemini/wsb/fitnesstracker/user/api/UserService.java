package com.capgemini.wsb.fitnesstracker.user.api;

/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    User createUser(User user);
    /**
     * Updates the information of an existing user.
     * <p>This method merges the details of the provided user entity with the
     * existing user record, applying only non-null fields.
     *
     * @param userId the ID of the user to update
     * @param user the user entity containing updated information
     * @return the updated {@link User} entity after applying changes
     */
    User updateUser(Long userId, User user);

}