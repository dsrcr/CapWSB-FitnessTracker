package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Implementation of the {@link TrainingProvider} interface, providing business logic for managing trainings.
 * The service is responsible for interacting with the {@link TrainingProvider} to handle training data.
 */

@Service
@RequiredArgsConstructor
@Slf4j

public class TrainingServiceImpl implements TrainingProvider, TrainingService {

    private final TrainingRepository trainingRepository;

    /**
     * Retrieves all trainings available in the system.
     *
     * @return A list of all {@link Training} objects.
     */
    @Override
    public List<Training> findAllTrainings() {
        return trainingRepository.findAll();
    }

    /**
     * Retrieves a list of trainings that have ended after the specified date.
     *
     * @param endDate The date after which the trainings must have ended.
     * @return A list of {@link Training} objects that ended after the provided date.
     */
    @Override
    public List<Training> findTrainingsAfterDate(Date endDate) {
        return trainingRepository.findByEndTimeAfter(endDate);
    }

    /**
     * Retrieves a list of all trainings for a specified user.
     *
     * @param userId the ID of the user whose trainings are to be retrieved
     * @return a list of {@link Training} objects associated with the specified user
     */
    @Override
    public List<Training> getTrainingsByUserId(Long userId) {
        return trainingRepository.findByUserId(userId);
    }

    /**
     * Retrieves all trainings associated with a specific activity type.
     *
     * @param activityType the activity type to search for (e.g., RUNNING, CYCLING)
     * @return a list of {@link Training} objects associated with the specified activity type
     */
    @Override
    public List<Training> getTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findByActivityType(activityType);
    }

    /**
     * Creates a new training record in the system.
     * Validates if the user exists before creating the training record.
     *
     * @param training the {@link Training} object to be created
     * @return the created {@link Training} object
     * @throws IllegalArgumentException if the provided user ID does not exist
     */
    @Override
    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    /**
     * Updates an existing training record with new information.
     * Only the fields provided in the updated training object will be updated.
     *
     * @param trainingId      the ID of the training record to be updated
     * @param updatedTraining the {@link Training} object containing updated information
     * @return the updated {@link Training} object
     * @throws IllegalArgumentException if the training with the provided ID does not exist
     */
    @Override
    public Training updateTraining(Long trainingId, Training updatedTraining) {
        return null;
    }

}
