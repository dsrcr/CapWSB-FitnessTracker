package com.capgemini.wsb.fitnesstracker.statistics.api;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing user statistics in the fitness tracker application.
 * This includes functionalities to create, update, delete, and retrieve statistics for users.
 */
@Service
public interface StatisticsService {

    /**
     * Create or update statistics for a user.
     * This method will either create new statistics if they don't exist,
     * or update the existing ones with the provided statistics data.
     *
     * @param statistics the statistics object to save or update
     * @return the saved or updated {@link Statistics} object
     * @throws IllegalArgumentException if the user associated with the statistics does not exist
     */
    Statistics saveStatistics(Statistics statistics);

    /**
     * Delete statistics by their ID.
     * This method removes the statistics associated with the provided statistics ID from the system.
     *
     * @param statisticsId the ID of the statistics to delete
     */
    void deleteStatisticsById(Long statisticsId);

    /**
     * Delete statistics by user ID and set them to zero.
     * This method resets the statistics for the specified user (i.e., sets total trainings,
     * total distance, and total calories burned to zero).
     *
     * @param userId the ID of the user whose statistics are to be deleted and reset
     * @return true if the statistics were found and reset, false if no statistics exist for the user
     */
    boolean deleteStatisticsByUserId(Long userId);

    /**
     * Update statistics for a specific user.
     * This method updates the statistics for the user based on their most recent training sessions.
     * It recalculates the total number of trainings, total distance, and total calories burned.
     *
     * @param userId the ID of the user whose statistics should be updated
     */
    void updateStatisticsForUser(Long userId);

    /**
     * Calculate the number of calories burned during a training session.
     * This method calculates the total calories burned based on the type of training and its associated distance.
     *
     * @param training the {@link Training} object representing the user's training session
     * @return the total number of calories burned during the specified training session
     */
    double calculateCaloriesBurned(Training training);
}
