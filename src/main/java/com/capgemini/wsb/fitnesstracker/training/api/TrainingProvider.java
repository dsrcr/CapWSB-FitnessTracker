package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;

import java.util.Date;
import java.util.List;

/**
 * Interface for managing and retrieving {@link Training} objects from a training provider.
 * This interface provides methods for retrieving training details based on the training ID and filtering
 * trainings by user or other criteria.
 */
public interface TrainingProvider {

    /**
     * Retrieves all trainings available in the system.
     *
     * @return A list of all {@link Training} objects.
     */
    List<Training> findAllTrainings();

    /**
     * Retrieves a list of trainings that have ended after the specified date.
     *
     * @param endDate The date after which the trainings must have ended.
     * @return A list of {@link Training} objects that ended after the provided date.
     */
    List<Training> findTrainingsAfterDate(Date endDate);

    /**
     * Retrieves a list of all trainings for a specified user.
     *
     * @param userId the ID of the user whose trainings are to be retrieved
     * @return a list of {@link Training} objects associated with the specified user
     */
    List<Training> getTrainingsByUserId(Long userId);

    /**
     * Retrieves all trainings associated with a specific activity type.
     *
     * @param activityType the activity type to search for (e.g., RUNNING, CYCLING)
     * @return a list of {@link Training} objects associated with the specified activity type
     */
    List<Training> getTrainingsByActivityType(ActivityType activityType);

    /**
     * Creates a new training entry.
     *
     * @param training the training to create
     * @return the created training
     */
    Training createTraining(Training training);

    /**
     * Updates an existing training entry.
     *
     * @param training the training to update
     * @return the updated training
     */
    Training updateTraining(Training training);
}
