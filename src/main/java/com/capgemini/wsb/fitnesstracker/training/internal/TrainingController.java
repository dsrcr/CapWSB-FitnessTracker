package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.TrainingDto;
import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing and retrieving training information.
 * Provides endpoints for retrieving all trainings, filtering by user, and filtering by date.
 */
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;

    /**
     * Retrieves all trainings available in the system and converts them to TrainingDto.
     *
     * @return A list of all {@link TrainingDto} objects.
     */
    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.findAllTrainings().stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of trainings associated with a specific user, identified by their user ID.
     * Converts the results to TrainingDto.
     *
     * @param userId the ID of the user whose trainings are to be retrieved
     * @return a list of {@link TrainingDto} objects associated with the specified user ID
     */
    @GetMapping("/{userId}")
    public List<TrainingDto> getTrainingsByUser(@PathVariable Long userId) {
        return trainingService.getTrainingsByUserId(userId).stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all trainings that have finished after the given time, converted to TrainingDto.
     *
     * @param afterTime the date after which the trainings must have ended (format: yyyy-MM-dd)
     * @return a list of {@link TrainingDto} objects that have ended after the given time
     */
    @GetMapping("/finished/{afterTime}")
    public List<TrainingDto> getFinishedTrainingsAfterTime(@PathVariable String afterTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date afterDate = sdf.parse(afterTime);
        return trainingService.findTrainingsAfterDate(afterDate).stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all trainings associated with a specific activity type (e.g., RUNNING, CYCLING),
     * and converts them to TrainingDto.
     *
     * @param activityType the activity type to search for (e.g., RUNNING, CYCLING)
     * @return a list of {@link TrainingDto} objects associated with the specified activity type
     */
    @GetMapping("/activityType")
    public List<TrainingDto> getTrainingsByActivityType(@RequestParam ActivityType activityType) {
        return trainingService.getTrainingsByActivityType(activityType).stream()
                .map(trainingMapper::toDto)
                .collect(Collectors.toList());
    }
}
