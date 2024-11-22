package com.capgemini.wsb.fitnesstracker.report.internal;

import com.capgemini.wsb.fitnesstracker.report.api.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @Mock
    private ReportServiceImpl reportService;

    @InjectMocks
    private ReportController reportController;


    @Test
    void testSendMonthlyReport() {
        int month = 10;
        int year = 2024;

        ResponseEntity<String> response = reportController.sendMonthlyReport(month, year);
        verify(reportService, times(1)).generateMonthlyReport(month, year);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Reports has been sent!", response.getBody());
    }
}
