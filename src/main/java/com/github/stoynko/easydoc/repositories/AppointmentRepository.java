package com.github.stoynko.easydoc.repositories;

import com.github.stoynko.easydoc.models.Appointment;
import com.github.stoynko.easydoc.models.Doctor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.models.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByDoctorAndStartsAtGreaterThanEqualOrderByStartsAtAsc(Doctor doctor, LocalDate date);

    List<Appointment> findByPatientAndStatusInOrderByStartsAtAsc(User patient, Collection<AppointmentStatus> statuses);

    List<Appointment> findByDoctorAndStatusInOrderByStartsAtAsc(Doctor doctor, Collection<AppointmentStatus> statuses);

    List<Appointment> findByDoctorAndStatusAndStartsAtBetweenOrderByStartsAtAsc(Doctor doctor, AppointmentStatus status,
                                                                                LocalDateTime start, LocalDateTime end
    );

    Optional<Appointment> findAppointmentById(UUID id);
}
