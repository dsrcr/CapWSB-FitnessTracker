package com.capgemini.wsb.fitnesstracker.report.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.internal.EmailSenderImpl;
import com.capgemini.wsb.fitnesstracker.report.api.ReportService;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingRepository;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the {@link ReportService} interface.
 * This service is responsible for generating reports and them with emails using {@link JavaMailSender}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final EmailSenderImpl emailSender;

    /**
     * Generates a monthly training report for each user.
     * The report contains the total number of trainings for each user within the specified month and year.
     */
    @Override
    public void generateMonthlyReport(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstOfMonth = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        Date lastOfMonth = calendar.getTime();

        List<User> users = userRepository.findAll();

        for (User user : users) {
            long trainingCount = trainingRepository.countByUserIdAndMonth(user.getId(), firstOfMonth, lastOfMonth);
            String emailContent = "You have registered " + trainingCount + " workouts this month.";
            EmailDto email = new EmailDto(user.getEmail(), "Monthly Training Report", emailContent);
            emailSender.send(email);
            log.info("Report sent to user: {}", user.getEmail());
        }
    }

    /**
     * This method is scheduled to run at the end of each month.
     * It calculates the last day of the current month and calls generateMonthlyReport
     * to send the reports for the current month.
     */
    @Scheduled(cron = "0 59 23 28-31 * ?")
    @Override
    public void sendMonthlyReportsAtMonthEnd() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        generateMonthlyReport(currentMonth, currentYear);
    }
}
