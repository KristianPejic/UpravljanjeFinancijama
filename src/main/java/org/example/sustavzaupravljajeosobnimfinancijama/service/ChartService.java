package org.example.sustavzaupravljajeosobnimfinancijama.service;

import org.example.sustavzaupravljajeosobnimfinancijama.dto.ChartDataResponse;

public interface ChartService {

    ChartDataResponse getSpendingByCategory(Long userId, int year, int month);

    ChartDataResponse getMonthlyTrend(Long userId, int year);
}
