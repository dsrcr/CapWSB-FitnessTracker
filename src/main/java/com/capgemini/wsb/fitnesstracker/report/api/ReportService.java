package com.capgemini.wsb.fitnesstracker.report.api;

public interface ReportService {

    /**
     * Generates a monthly training report for each user.
     * The report contains the total number of trainings for each user within the specified month and year.
     *
     * @param month the month for which the report is to be generated (1 for January, 2 for February, etc.)
     * @param year  the year for which the report is to be generated
     */
    void generateMonthlyReport(int month, int year);

    /**
     * This method is scheduled to run at the end of each month.
     * It calculates the last day of the current month and calls generateMonthlyReport
     * to send the reports for the current month.
     */
    void sendMonthlyReportsAtMonthEnd();
}
