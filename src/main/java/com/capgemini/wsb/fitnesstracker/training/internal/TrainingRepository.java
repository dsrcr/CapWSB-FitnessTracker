package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

interface TrainingRepository extends JpaRepository<Training, Long> {

    /**
     * Retrieves a list of trainings by the user ID.
     *
     * @param userId the user ID to search for
     * @return a list of Trainings that belong to the user with the given ID
     */
    List<Training> findByUserId(Long userId);

    /**
     * Retrieves all trainings that are finished after the specified date.
     *
     * @param endDate the date after which the training must have ended
     * @return a list of Trainings that have an end time after the specified date
     */
    List<Training> findByEndTimeAfter(Date endDate);

    /**
     * Retrieves all trainings that are of a specified activity type.
     *
     * @param activityType the activity type to search for (e.g., RUNNING, CYCLING)
     * @return a list of {@link Training} objects associated with the specified activity type
     */
    List<Training> findByActivityType(ActivityType activityType);
}
