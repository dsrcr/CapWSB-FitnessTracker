package com.capgemini.wsb.fitnesstracker.statistics.api;

import java.util.List;
import java.util.Optional;

public interface StatisticsProvider {

    /**
     * Retrieves a statistics based on their ID.
     * If the user with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param statisticsId id of the statistics to be searched
     * @return An {@link Optional} containing the located Statistics, or {@link Optional#empty()} if not found
     */
    Optional<Statistics> getStatisticsById(Long statisticsId);

    List<Statistics> getAllStatistics();

    /**
     * Retrieve all statistics where totalCaloriesBurned is greater than the specified value.
     *
     * @param calories the minimum number of calories to filter by
     * @return a list of statistics with calories burned greater than the specified value
     */
    List<Statistics> getStatisticsByCaloriesGreaterThan(int calories);

}
