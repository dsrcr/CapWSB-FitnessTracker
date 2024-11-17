package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDto;
import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserRepository userRepository;

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

    /**
     * Handles the creation of a new training.
     * <p>
     * This method maps the incoming TrainingDto object to a Training entity
     * and passes it to the training service to save it in the database.
     *
     * @param trainingDto the data transfer object containing the training details
     * @return the created Training entity
     */
    @PostMapping
    public Training createTraining(@RequestBody TrainingDto trainingDto) {
        User user = userRepository.findById(trainingDto.user().id())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Training training = trainingMapper.toEntity(trainingDto);
        training.setUser(user);

        return trainingService.createTraining(training);
    }

    /**
     * Updates an existing training with the given ID.
     * <p>
     * This method maps the incoming TrainingDto object to a Training entity,
     * sets the ID of the entity to match the provided path variable, and
     * passes it to the training service for updating in the database.
     *
     * @param id the ID of the training to be updated
     * @param trainingDto the data transfer object containing the updated training details
     * @return the updated Training entity
     */
    @PutMapping("/{id}")
    public Training updateTraining(@PathVariable Long id, @RequestBody TrainingDto trainingDto) {
        Long userId = trainingDto.user().id();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
        Training training = trainingMapper.toEntity(trainingDto);
        training.setId(id);

        return trainingService.updateTraining(training);
    }
}
