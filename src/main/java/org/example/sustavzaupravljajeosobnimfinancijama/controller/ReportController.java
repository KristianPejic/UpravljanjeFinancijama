package org.example.sustavzaupravljajeosobnimfinancijama.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.MonthlyReportResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.YearlyReportResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Financial report endpoints")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Get monthly financial report")
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getMonthlyReport(userId, year, month));
    }

    @Operation(summary = "Get yearly financial report")
    @GetMapping("/yearly")
    public ResponseEntity<YearlyReportResponse> getYearlyReport(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam int year) {
        return ResponseEntity.ok(reportService.getYearlyReport(userId, year));
    }
}
