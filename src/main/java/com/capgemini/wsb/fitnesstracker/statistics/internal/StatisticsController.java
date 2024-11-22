package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDto;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsMapper;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class responsible for managing statistics related to users.
 * It provides endpoints to retrieve, create, update, and delete user statistics.
 */
@RestController
@RequestMapping("/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsServiceImpl statisticsService;
    private final UserServiceImpl userService;

    /**
     * Retrieves all statistics for all users.
     *
     * @return a ResponseEntity containing a list of all statistics in DTO format
     */
    @GetMapping
    public ResponseEntity<List<StatisticsDto>> getAllStatistics() {
        List<Statistics> statistics = statisticsService.getAllStatistics();
        List<StatisticsDto> statisticsDto = statistics.stream()
                .map(StatisticsMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(statisticsDto);
    }

    /**
     * Retrieves the statistics for a specific user based on their user ID.
     *
     * @param userId the user ID whose statistics are to be retrieved
     * @return a ResponseEntity containing the user's statistics in DTO format if found,
     * or a 404 Not Found response if no statistics are found for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<StatisticsDto> getStatisticsByUserId(@PathVariable Long userId) {
        Optional<Statistics> statistics = statisticsService.getStatisticsById(userId);
        return statistics
                .map(stat -> ResponseEntity.ok(StatisticsMapper.toDto(stat)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates or updates the statistics for a user.
     * This endpoint checks if the user ID is valid and associates the statistics with the user.
     *
     * @param statisticsDto the statistics to be created or updated in DTO format
     * @return a ResponseEntity containing the saved statistics in DTO format
     * or a 400 Bad Request response if the user ID is null or invalid
     * @throws IllegalArgumentException if the user ID is not provided or not found
     */
    @PostMapping
    public ResponseEntity<StatisticsDto> saveStatistics(@RequestBody StatisticsDto statisticsDto) {
        if (statisticsDto.user() == null || statisticsDto.user().id() == null)
            throw new IllegalArgumentException("User ID must not be null");

        User user = userService.getUser(statisticsDto.user().id())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + statisticsDto.user().id()));

        Statistics statistics = StatisticsMapper.toEntity(statisticsDto);
        statistics.setUser(user);
        Statistics savedStatistics = statisticsService.saveStatistics(statistics);

        return ResponseEntity.ok(StatisticsMapper.toDto(savedStatistics));
    }

    /**
     * Deletes the statistics for a user by their user ID, setting their statistics to zero.
     *
     * @param userId the user ID whose statistics are to be deleted
     * @return a ResponseEntity with a 204 No Content status if the statistics were successfully deleted,
     * or a 404 Not Found response if no statistics were found for the user
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteStatisticsByUserId(@PathVariable Long userId) {
        if (statisticsService.deleteStatisticsByUserId(userId))
            return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }

    /**
     * Updates statistics for a specific user based on their user ID.
     * This endpoint updates the statistics if the user is found.
     *
     * @param userId the user ID whose statistics should be updated
     * @return a ResponseEntity indicating the status of the update operation,
     * with a 200 OK if successful or a 404 Not Found if the user ID is invalid
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<Void> updateStatisticsForUser(@PathVariable Long userId) {
        try {
            statisticsService.updateStatisticsForUser(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Retrieves statistics for users who have burned more calories than a specified value.
     *
     * @param calories the minimum number of calories burned to filter statistics by
     * @return a ResponseEntity containing a list of statistics where the calories burned exceed the specified value,
     * or a 204 No Content response if no such statistics are found
     */
    @GetMapping("/calories/{calories}")
    public ResponseEntity<List<StatisticsDto>> getStatisticsWithCaloriesGreaterThan(@PathVariable int calories) {
        List<Statistics> statisticsList = statisticsService.getStatisticsByCaloriesGreaterThan(calories);
        if (statisticsList.isEmpty())
            return ResponseEntity.noContent().build();

        List<StatisticsDto> statisticsDto = statisticsList.stream()
                .map(StatisticsMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(statisticsDto);
    }
}
