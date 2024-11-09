package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.api.Training;

/**
 * Service interface for managing training records.
 * It defines the operations for creating, updating, retrieving, and managing training data.
 */
public interface TrainingService {

    /**
     * Creates a new training record in the system.
     * Validates if the user exists before creating the training record.
     *
     * @param training the {@link Training} object to be created
     * @return the created {@link Training} object
     * @throws IllegalArgumentException if the provided user ID does not exist
     */
    Training createTraining(Training training);

    /**
     * Updates an existing training record with new information.
     * Only the fields provided in the updated training object will be updated.
     *
     * @param trainingId      the ID of the training record to be updated
     * @param updatedTraining the {@link Training} object containing updated information
     * @return the updated {@link Training} object
     * @throws IllegalArgumentException if the training with the provided ID does not exist
     */
    Training updateTraining(Long trainingId, Training updatedTraining);
}
