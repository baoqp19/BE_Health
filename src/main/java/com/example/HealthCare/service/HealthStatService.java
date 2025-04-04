package com.example.HealthCare.service;

import com.example.HealthCare.model.HealthStat;

import java.util.List;

public interface HealthStatService {
    HealthStat addHealthStat(HealthStat healthStat);
    HealthStat updateHealthStat(HealthStat healthStat);
    void deleteHealthStat(Integer statID);
    HealthStat getHealthStatById(Integer statID);
    List<HealthStat> getAllHealthStats(Integer id, String date);
    List<HealthStat> getHealthStatsByStatType(Integer id, String statType, String date);
    List<HealthStat> getHealthStatsToDisplayChart(Integer id, String statType, String date);
}



