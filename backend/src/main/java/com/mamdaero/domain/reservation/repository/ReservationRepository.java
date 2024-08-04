package com.mamdaero.domain.reservation.repository;

import com.mamdaero.domain.reservation.dto.response.ReservationListResponse;
import com.mamdaero.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT new com.mamdaero.domain.reservation.dto.response.ReservationListResponse(r.id, wt.date, wt.time, r.status, ci.name, ci.fee, r.canceler, r.canceledAt, r.requirement, r.isDiaryShared) " +
            "FROM Reservation r " +
            "JOIN WorkTime wt ON r.workTimeId = wt.id " +
            "JOIN CounselorItem ci ON r.counselorItemId = ci.counselorItemId " +
            "WHERE r.memberId = :memberId")
    List<ReservationListResponse> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT new com.mamdaero.domain.reservation.dto.response.ReservationListResponse(r.id, wt.date, wt.time, r.status, ci.name, ci.fee, r.canceler, r.canceledAt, r.requirement, r.isDiaryShared) " +
            "FROM Reservation r " +
            "JOIN WorkTime wt ON r.workTimeId = wt.id " +
            "JOIN CounselorItem ci ON r.counselorItemId = ci.counselorItemId " +
            "WHERE ci.counselorId = :counselorId")
    List<ReservationListResponse> findByCounselorId(@Param("counselorId") Long counselorId);
}

