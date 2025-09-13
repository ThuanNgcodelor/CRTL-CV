package com.example.userservice.repository;

import com.example.userservice.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findAllByOwner_IdOrderByStartTimeDesc(String ownerId);
    List<Appointment> findAllByVet_IdOrderByStartTimeDesc(String vetId);

    @Query("SELECT CASE WHEN COUNT(a)>0 THEN true ELSE false END FROM Appointment a " +
            "WHERE a.vet.id = :vetId AND a.startTime < :endTime AND a.endTime > :startTime")
    boolean existsVetOverlap(String vetId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT CASE WHEN COUNT(a)>0 THEN true ELSE false END FROM Appointment a " +
            "WHERE a.owner.id = :ownerId AND a.startTime < :endTime AND a.endTime > :startTime")
    boolean existsOwnerOverlap(String ownerId, LocalDateTime startTime, LocalDateTime endTime);
}