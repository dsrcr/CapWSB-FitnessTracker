package com.capgemini.wsb.fitnesstracker.statistics.api;

import com.capgemini.wsb.fitnesstracker.user.internal.UserDto;
import jakarta.annotation.Nullable;

/**
 * Data Transfer Object (DTO) representing the statistics for a user.
 * This object is used to transfer statistics data between layers or services.
 * It includes information about a user's total trainings, distance, and calories burned.
 */

public record StatisticsDto(
        @Nullable Long id,
        UserDto user,
        int totalTrainings,
        double totalDistance,
        int totalCaloriesBurned
) {
}