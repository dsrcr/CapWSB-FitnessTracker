package com.capgemini.wsb.fitnesstracker.statistics.api;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserDto;

/**
 * Utility class for mapping between {@link Statistics} entities and {@link StatisticsDto} objects.
 * This class provides methods to convert between the domain model (entity) and data transfer object (DTO),
 * which is useful for separating concerns in the application layer and improving the performance of API responses.
 */
public class StatisticsMapper {

    /**
     * Converts a {@link Statistics} entity to a {@link StatisticsDto}.
     * This method maps the entity's fields to the DTO's corresponding fields, including user details.
     *
     * @param statistics the {@link Statistics} entity to be converted to a DTO
     * @return a {@link StatisticsDto} containing the data from the given {@link Statistics} entity
     */
    public static StatisticsDto toDto(Statistics statistics) {
        User user = statistics.getUser();
        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getBirthdate(), user.getEmail());

        return new StatisticsDto(
                statistics.getId(),
                userDto,
                statistics.getTotalTrainings(),
                statistics.getTotalDistance(),
                statistics.getTotalCaloriesBurned()
        );
    }

    /**
     * Converts a {@link StatisticsDto} to a {@link Statistics} entity.
     * This method maps the DTO's fields to the entity's corresponding fields.
     *
     * @param statisticsDto the {@link StatisticsDto} to be converted to an entity
     * @return a {@link Statistics} entity populated with the data from the given DTO
     */
    public static Statistics toEntity(StatisticsDto statisticsDto) {
        User user = new User(
                statisticsDto.user().firstName(),
                statisticsDto.user().lastName(),
                statisticsDto.user().birthdate(),
                statisticsDto.user().email()
        );

        return new Statistics(
                user,
                statisticsDto.totalTrainings(),
                statisticsDto.totalDistance(),
                statisticsDto.totalCaloriesBurned()
        );
    }
}
