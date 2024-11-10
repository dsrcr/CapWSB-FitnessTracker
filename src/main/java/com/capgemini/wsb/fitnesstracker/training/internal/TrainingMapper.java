package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDto;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserDto;
import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper component that handles the conversion between {@link Training} entities and {@link TrainingDto} data transfer objects.
 * <p>
 * This class is designed to facilitate the transformation of Training entities into a structure suitable
 * for API responses and vice versa. The {@link UserMapper} is used to convert the nested {@link User} entity
 * to and from a {@link UserDto}, maintaining consistent handling of user information across the application.
 */
@Component
public class TrainingMapper {

    private final UserMapper userMapper;

    public TrainingMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Converts a Training entity to a TrainingDto.
     *
     * @param training the Training entity to convert
     * @return a TrainingDto containing training information
     */
    public TrainingDto toDto(Training training) {
        return new TrainingDto(
                training.getId(),
                userMapper.toDto(training.getUser()),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        );
    }

    /**
     * Converts a TrainingDto to a Training entity.
     *
     * @param trainingDto the TrainingDto to convert
     * @return a Training entity created from the TrainingDto data
     */
    public Training toEntity(TrainingDto trainingDto) {
        return new Training(
                userMapper.toEntity(trainingDto.user()),
                trainingDto.startTime(),
                trainingDto.endTime(),
                trainingDto.activityType(),
                trainingDto.distance(),
                trainingDto.averageSpeed()
        );
    }
}
