package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsProvider;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsService;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingRepository;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for handling statistics-related operations.
 * This service handles CRUD operations for user statistics and provides functionality
 * such as retrieving, creating, updating, and deleting user statistics.
 * It also provides functionality to calculate calories burned based on different activity types.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService, StatisticsProvider {

    private final StatisticsRepository statisticsRepository;
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    /**
     * Retrieves a statistics based on user ID.
     * If the user with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param userId id of the statistics to be searched
     * @return An {@link Optional} containing the located Statistics, or {@link Optional#empty()} if not found
     */
    @Override
    public Optional<Statistics> getStatisticsById(Long userId) {
        return statisticsRepository.findByUserId(userId);
    }

    /**
     * Retrieves a statistics based on their ID.
     * If the user with given ID is not found, then {@link Optional#empty()} will be returned.
     */
    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAll();
    }

    /**
     * Retrieve all statistics where totalCaloriesBurned is greater than the specified value.
     *
     * @param calories the minimum number of calories to filter by
     * @return a list of statistics with calories burned greater than the specified value
     */
    @Override
    public List<Statistics> getStatisticsByCaloriesGreaterThan(int calories) {
        return statisticsRepository.findByTotalCaloriesBurnedGreaterThan(calories);
    }

    /**
     * Create or update statistics for a user.
     * This will either create new statistics if they don't exist,
     * or update existing ones.
     *
     * @param statistics the statistics object to save or update
     * @return the saved or updated statistics
     */
    @Override
    @Transactional
    public Statistics saveStatistics(Statistics statistics) {
        if (statistics.getUser() == null)
            throw new IllegalArgumentException("User must not be null for statistics");

        Long userId = statistics.getUser().getId();
        if (userId == null)
            throw new IllegalArgumentException("User ID must not be null");

        if (userRepository.findById(userId).isEmpty())
            throw new IllegalArgumentException("User does not exist for statistics");

        Optional<Statistics> existingStatistics = statisticsRepository.findByUserId(userId);
        if (existingStatistics.isPresent()) {
            Statistics stats = existingStatistics.get();
            stats.setTotalTrainings(statistics.getTotalTrainings());
            stats.setTotalDistance(statistics.getTotalDistance());
            stats.setTotalCaloriesBurned(statistics.getTotalCaloriesBurned());

            return statisticsRepository.save(stats);
        }

        return statisticsRepository.save(statistics);
    }


    /**
     * Delete statistics by their ID.
     *
     * @param statisticsId the ID of the statistics to delete
     */
    @Override
    public void deleteStatisticsById(Long statisticsId) {
        statisticsRepository.deleteById(statisticsId);
    }

    /**
     * Delete statistics by user ID.
     *
     * @param userId the ID of the user whose statistics are to be deleted
     */
    @Override
    @Transactional
    public boolean deleteStatisticsByUserId(Long userId) {
        Optional<Statistics> existingStatistics = statisticsRepository.findByUserId(userId);

        if (existingStatistics.isPresent()) {
            Statistics stats = existingStatistics.get();

            stats.setTotalTrainings(0);
            stats.setTotalDistance(0.0);
            stats.setTotalCaloriesBurned(0);

            statisticsRepository.save(stats);
            return true;
        }

        return false;
    }

    /**
     * Updates statistics for a user based on their training data.
     * If statistics already exist for the user, they are updated.
     * If statistics do not exist, a new record is created and saved.
     *
     * @param userId the ID of the user whose statistics are to be updated
     */
    @Override
    @Transactional
    public void updateStatisticsForUser(Long userId) {
        List<Training> userTrainings = trainingRepository.findByUserId(userId);
        int totalTrainings = userTrainings.size();
        double totalDistance = userTrainings.stream().mapToDouble(Training::getDistance).sum();
        double totalCaloriesBurned = 0;

        for (Training training : userTrainings)
            totalCaloriesBurned += calculateCaloriesBurned(training);

        Optional<Statistics> existingStatistics = statisticsRepository.findByUserId(userId);
        Statistics statistics;

        if (existingStatistics.isPresent()) {
            statistics = existingStatistics.get();
            statistics.setTotalTrainings(totalTrainings);
            statistics.setTotalDistance(totalDistance);
            statistics.setTotalCaloriesBurned((int) totalCaloriesBurned);
        } else {
            statistics = new Statistics(
                    userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found")),
                    totalTrainings,
                    totalDistance,
                    (int) totalCaloriesBurned
            );
        }

        statisticsRepository.save(statistics);
    }

    /**
     * Calculates the number of calories burned during a given training session.
     * The calorie burn is calculated based on the activity type (Running, Cycling, Walking, etc.)
     *
     * @param training the training session for which to calculate calories burned
     * @return the number of calories burned during the training session
     */
    @Override
    public double calculateCaloriesBurned(Training training) {
        return switch (training.getActivityType()) {
            case RUNNING -> training.getDistance() * 60;
            case CYCLING -> training.getDistance() * 40;
            case WALKING -> training.getDistance() * 35;
            default -> training.getDistance() * 50;
        };
    }
}