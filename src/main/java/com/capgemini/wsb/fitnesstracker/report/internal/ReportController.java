package com.capgemini.wsb.fitnesstracker.report.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for handling report generation and email sending.
 * Provides an endpoint for triggering the generation and sending of monthly training reports.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reports")
public class ReportController {

    private final ReportServiceImpl reportService;

    /**
     * Endpoint to manually trigger the sending of a monthly training report.
     * This will generate the report for the given month and year and send it via email to each user.
     *
     * @param month the month for which the report is to be generated (1 for January, 2 for February, etc.)
     * @param year  the year for which the report is to be generated
     * @return a response indicating that the report has been sent
     */
    @PostMapping("/send-monthly-report")
    public ResponseEntity<String> sendMonthlyReport(@RequestParam int month, @RequestParam int year) {
        reportService.generateMonthlyReport(month, year);
        return ResponseEntity.ok("Reports has been sent!");
    }
}
