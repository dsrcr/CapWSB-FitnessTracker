package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    /**
     * Find statistics by user ID.
     *
     * @param userId the ID of the user whose statistics are to be retrieved
     * @return an optional containing the statistics, if found
     */
    Optional<Statistics> findByUserId(Long userId);

    /**
     * Retrieves all statistics where calories are greater than the specified value.
     *
     * @param calories the minimum number of calories burned to filter by
     * @return a list of statistics where calories burned is greater than the specified value
     */
    List<Statistics> findByTotalCaloriesBurnedGreaterThan(int calories);

}
