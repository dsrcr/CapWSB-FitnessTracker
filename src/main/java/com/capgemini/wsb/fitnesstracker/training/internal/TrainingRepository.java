package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {

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

    /**
     * Retrieves the count of trainings for a specific user in a given month and year.
     *
     * @param userId       the ID of the user
     * @param startOfMonth the start date of the month
     * @param endOfMonth   the end date of the month
     * @return the count of trainings
     */
    @Query("SELECT COUNT(t) FROM Training t WHERE t.user.id = :userId AND t.startTime BETWEEN :startOfMonth AND :endOfMonth")
    long countByUserIdAndMonth(@Param("userId") Long userId,
                               @Param("startOfMonth") Date startOfMonth,
                               @Param("endOfMonth") Date endOfMonth);
}
