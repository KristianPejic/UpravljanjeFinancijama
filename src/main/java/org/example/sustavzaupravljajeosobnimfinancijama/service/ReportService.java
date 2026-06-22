package org.example.sustavzaupravljajeosobnimfinancijama.service;

import org.example.sustavzaupravljajeosobnimfinancijama.dto.MonthlyReportResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.YearlyReportResponse;

public interface ReportService {

    MonthlyReportResponse getMonthlyReport(Long userId, int year, int month);

    YearlyReportResponse getYearlyReport(Long userId, int year);
}
