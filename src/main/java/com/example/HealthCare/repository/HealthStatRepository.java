package com.example.HealthCare.repository;

import com.example.HealthCare.model.HealthStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HealthStatRepository extends JpaRepository<HealthStat, Integer> {
    @Query("SELECT h FROM HealthStat h WHERE DATE(h.date) = :date")
    List<HealthStat> findByDate(@Param("date") LocalDateTime date);

    @Query("SELECT h \n" +
            "FROM HealthStat h \n" +
            "WHERE h.member.id = :memberId \n" +
            "  AND h.statType = :statType \n" +
            "  AND DATE(h.date) >= :date \n" +
            "  AND DATE(h.date) <= CURRENT_DATE\n" +
            "ORDER BY h.date ASC\n")
    List<HealthStat> getAllHealthStatsByStatTypeHasDate
            (@Param("memberId") Integer memberId,
             @Param("statType") String statType,
             @Param("date") LocalDate date);

    @Query("SELECT h FROM HealthStat h WHERE h.member.id = :memberId AND h.statType = :statType ORDER BY h.date DESC")
    List<HealthStat> getAllHealthStatsByStatType
            (@Param("memberId") Integer memberId,
             @Param("statType") String statType);

    List<HealthStat> findAllById(Integer id);
}
