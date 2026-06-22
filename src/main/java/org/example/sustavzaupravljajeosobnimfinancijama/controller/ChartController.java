package org.example.sustavzaupravljajeosobnimfinancijama.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.ChartDataResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.security.CustomUserDetails;
import org.example.sustavzaupravljajeosobnimfinancijama.service.ChartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/charts")
@RequiredArgsConstructor
@Tag(name = "Charts", description = "Chart data endpoints for visualization")
public class ChartController {

    private final ChartService chartService;

    @Operation(summary = "Get spending by category (pie chart data)")
    @GetMapping("/spending-by-category")
    public ResponseEntity<ChartDataResponse> getSpendingByCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(chartService.getSpendingByCategory(userDetails.getId(), year, month));
    }

    @Operation(summary = "Get monthly income vs expense trend (bar chart data)")
    @GetMapping("/monthly-trend")
    public ResponseEntity<ChartDataResponse> getMonthlyTrend(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int year) {
        return ResponseEntity.ok(chartService.getMonthlyTrend(userDetails.getId(), year));
    }
}
