package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controller for managing and retrieving training information.
 * Provides endpoints for retrieving all trainings, filtering by user, and filtering by date.
 */
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingServiceImpl trainingService;

    /**
     * Retrieves all trainings available in the system.
     *
     * @return A list of all {@link Training} objects.
     */
    @GetMapping
    public List<Training> getAllTrainings() {
        return trainingService.findAllTrainings();
    }

    /**
     * Retrieves a list of trainings associated with a specific user, identified by their user ID.
     * This endpoint allows clients to retrieve all trainings for a given user.
     *
     * @param userId the ID of the user whose trainings are to be retrieved
     * @return a list of {@link Training} objects associated with the specified user ID
     */
    @GetMapping("/{userId}")
    public List<Training> getTrainingsByUser(@PathVariable Long userId) {
        return trainingService.getTrainingsByUserId(userId);
    }

    /**
     * Retrieves all trainings that have finished after the given time.
     *
     * @param afterTime the date after which the trainings must have ended
     * @return a list of Trainings that have ended after the given time
     */
    @GetMapping("/finished/{afterTime}")
    public List<Training> getFinishedTrainingsAfterTime(@PathVariable String afterTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date afterDate = sdf.parse(afterTime);
        return trainingService.findTrainingsAfterDate(afterDate);
    }

    /**
     * Retrieves all trainings associated with a specific activity type (e.g., RUNNING, CYCLING).
     *
     * @param activityType the activity type to search for (e.g., RUNNING, CYCLING)
     * @return a list of {@link Training} objects associated with the specified activity type
     */
    @GetMapping("/activityType")
    public List<Training> getTrainingsByActivityType(@RequestParam ActivityType activityType) {
        return trainingService.getTrainingsByActivityType(activityType);
    }
}
