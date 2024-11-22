package com.capgemini.wsb.fitnesstracker.loader;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;
import static java.util.Objects.isNull;

/**
 * Sample init data loader. If the application is run with `loadInitialData` profile,
 * then on application startup it will fill the database with dummy data,
 * for the manual testing purposes.
 * Loader is triggered by {@link ContextRefreshedEvent } event
 */
@Component
@Profile("loadInitialData")
@Slf4j
@ToString
public class InitialDataLoader {

    @Autowired
    private JpaRepository<User, Long> userRepository;

    @Autowired
    private JpaRepository<Training, Long> trainingRepository;

    @Autowired
    private JpaRepository<Statistics, Long> statisticsRepository;

    @EventListener
    @Transactional
    public void loadInitialData(ContextRefreshedEvent event) {
        verifyDependenciesAutowired();
        log.info("Loading initial data to the database");
        List<User> sampleUserList = generateSampleUsers();
        List<Training> sampleTrainingList = generateTrainingData(sampleUserList);
        generateAndSaveStatistics(sampleUserList);
        log.info("Finished loading initial data");
    }

    private User generateUser(String name, String lastName, int age) {
        User user = new User(name,
                lastName,
                now().minusYears(age),
                "%s.%s@domain.com".formatted(name, lastName));
        return userRepository.save(user);
    }

    private List<User> generateSampleUsers() {
        List<User> users = new ArrayList<>();

        users.add(generateUser("Emma", "Johnson", 28));
        users.add(generateUser("Ethan", "Taylor", 51));
        users.add(generateUser("Olivia", "Davis", 76));
        users.add(generateUser("Daniel", "Thomas", 34));
        users.add(generateUser("Sophia", "Baker", 49));
        users.add(generateUser("Liam", "Jones", 23));
        users.add(generateUser("Ava", "Williams", 21));
        users.add(generateUser("Noah", "Miller", 39));
        users.add(generateUser("Grace", "Anderson", 33));
        users.add(generateUser("Oliver", "Swift", 29));

        return users;
    }

    private List<Training> generateTrainingData(List<User> users) {
        List<Training> trainingData = new ArrayList<>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            trainingData.add(createTraining(users.get(0), sdf, "2024-01-19 08:00:00", "2024-01-19 09:30:00", ActivityType.RUNNING, 10.5, 8.2));
            trainingData.add(createTraining(users.get(1), sdf, "2024-01-18 15:30:00", "2024-01-18 17:00:00", ActivityType.CYCLING, 25.0, 18.5));
            trainingData.add(createTraining(users.get(2), sdf, "2024-01-17 07:45:00", "2024-01-17 09:00:00", ActivityType.WALKING, 5.2, 5.8));
            trainingData.add(createTraining(users.get(3), sdf, "2024-01-16 18:00:00", "2024-01-16 19:30:00", ActivityType.RUNNING, 12.3, 9.0));
            trainingData.add(createTraining(users.get(4), sdf, "2024-01-15 12:30:00", "2024-01-15 13:45:00", ActivityType.CYCLING, 18.7, 15.3));
            trainingData.add(createTraining(users.get(5), sdf, "2024-01-14 09:00:00", "2024-01-14 10:15:00", ActivityType.WALKING, 3.5, 4.0));
            trainingData.add(createTraining(users.get(6), sdf, "2024-01-13 16:45:00", "2024-01-13 18:30:00", ActivityType.RUNNING, 15.0, 10.8));
            trainingData.add(createTraining(users.get(7), sdf, "2024-01-12 11:30:00", "2024-01-12 12:45:00", ActivityType.CYCLING, 22.5, 17.2));
            trainingData.add(createTraining(users.get(8), sdf, "2024-01-11 07:15:00", "2024-01-11 08:30:00", ActivityType.WALKING, 4.2, 4.5));
            trainingData.add(createTraining(users.get(9), sdf, "2024-01-10 14:00:00", "2024-01-10 15:15:00", ActivityType.RUNNING, 11.8, 8.5));

            trainingData.forEach(trainingRepository::save);
        } catch (ParseException e) {
            log.error("Error parsing date", e);
        }

        return trainingData;
    }

    private Training createTraining(User user, SimpleDateFormat sdf, String startDate, String endDate, ActivityType activityType, double distance, double averageSpeed) throws ParseException {
        return new Training(user,
                sdf.parse(startDate),
                sdf.parse(endDate),
                activityType,
                distance,
                averageSpeed);
    }

    private void generateAndSaveStatistics(List<User> users) {
        for (User user : users) {
            List<Training> userTrainings = trainingRepository.findAll();

            double totalDistance = 0;
            int totalCaloriesBurned = 0;
            int trainingCount = 0;

            for (Training training : userTrainings) {
                if (training.getUser().equals(user)) {
                    totalDistance += training.getDistance();
                    totalCaloriesBurned += calculateCaloriesBurned(training.getDistance());
                    trainingCount++;
                }
            }

            if (trainingCount > 0) {
                Statistics statistics = new Statistics(
                        user,
                        trainingCount,
                        totalDistance,
                        totalCaloriesBurned
                );

                statisticsRepository.save(statistics);
            }
        }
    }

    private int calculateCaloriesBurned(double distance) {
        // @TODO adjust if needed
        int multiplier = 50;
        return (int) (distance * multiplier);
    }

    private void verifyDependenciesAutowired() {
        if (isNull(userRepository) || isNull(trainingRepository) || isNull(statisticsRepository)) {
            throw new IllegalStateException("Initial data loader was not autowired correctly " + this);
        }
    }
}
