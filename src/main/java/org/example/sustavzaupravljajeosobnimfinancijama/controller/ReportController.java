package org.example.sustavzaupravljajeosobnimfinancijama.controller;

import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.MonthlyReportResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.YearlyReportResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getMonthlyReport(userId, year, month));
    }

    @GetMapping("/yearly")
    public ResponseEntity<YearlyReportResponse> getYearlyReport(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam int year) {
        return ResponseEntity.ok(reportService.getYearlyReport(userId, year));
    }
}
