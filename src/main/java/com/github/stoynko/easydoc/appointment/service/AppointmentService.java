package com.github.stoynko.easydoc.appointment.service;

import com.github.stoynko.easydoc.appointment.event.AppointmentCompletedEvent;
import com.github.stoynko.easydoc.appointment.exception.AppointmentDoesNotExistException;
import com.github.stoynko.easydoc.appointment.exception.InvalidTimeException;
import com.github.stoynko.easydoc.appointment.exception.PastDateException;
import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.appointment.repository.AppointmentRepository;
import com.github.stoynko.easydoc.appointment.web.dto.request.AppointmentRequest;
import com.github.stoynko.easydoc.appointment.web.dto.response.AppointmentTimeSlotResponse;
import com.github.stoynko.easydoc.practitioner.model.Doctor;
import com.github.stoynko.easydoc.practitioner.service.DoctorService;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.stoynko.easydoc.appointment.model.AppointmentStatus.CANCELLED;
import static com.github.stoynko.easydoc.appointment.model.AppointmentStatus.COMPLETED;
import static com.github.stoynko.easydoc.appointment.model.AppointmentStatus.NO_SHOW;
import static com.github.stoynko.easydoc.appointment.model.AppointmentStatus.PENDING;
import static com.github.stoynko.easydoc.appointment.model.AppointmentStatus.PROCESSING;
import static com.github.stoynko.easydoc.appointment.web.mapper.AppointmentMapper.toAppointmentEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME = LocalTime.of(15, 40);
    private static final int EXAMINATION_INTERVAL = 20;
    private final AppointmentRepository repository;
    private final DoctorService doctorService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    public void createAppointment(UUID patientId, UUID doctorId, AppointmentRequest request) {

        validateDateAndTime(request.getDate(), request.getTime());

        User patient = userService.getUserById(patientId);
        Doctor doctor = doctorService.getDoctorByDoctorId(doctorId);

        Appointment appointment = toAppointmentEntity(request, patient, doctor);

        repository.save(appointment);
        log.info("[appointment-created] Appointment was successfully created for patient with id {}", patientId);
    }

    public void processAppointment(UUID appointmentId, UserAuthenticationDetails principal) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(PROCESSING);
        repository.save(appointment);
        log.info("[appointment-processed] Appointment processing was started for {}", appointmentId);
    }

    @Transactional
    public void concludeAppointment(UUID appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(COMPLETED);
        saveAppointment(appointment);

        log.info("[appointment-completed] | Appointment {} was successfully concluded at {}", appointment.getId(), LocalDateTime.now());
        eventPublisher.publishEvent(new AppointmentCompletedEvent(appointment.getPatient().getEmailAddress(), appointmentId));
    }

    public void cancelAppointment(UUID appointmentId, UserAuthenticationDetails principal) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(CANCELLED);
        repository.save(appointment);
        log.info("[appointment-cancellation] | Appointment {} was successfully canceled at {}", appointment.getId(), LocalDateTime.now());
    }

    public void markAsNoShow(UUID appointmentId, UserAuthenticationDetails principal) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(NO_SHOW);
        repository.save(appointment);
        log.info("[appointment-noshow] | Appointment {} was marked as no-show at {}", appointment.getId(), LocalDateTime.now());
    }

    public void saveAppointment(Appointment appointment) {
        repository.save(appointment);
    }

    public void addPrescriptionToAppointment(UUID appointmentId, UUID prescriptionId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setPrescription(prescriptionId);
        repository.save(appointment);
        log.info("[added-prescription] | Prescription {} was successfully added to appointment {}", prescriptionId, appointment.getId());
    }

    public Appointment getAppointmentById(UUID id) {
        return repository.findAppointmentById(id)
                .orElseThrow(() -> new AppointmentDoesNotExistException());
    }

    public List<Appointment> getAppointmentsFor(UUID doctorId, LocalDate date) {
        Doctor doctor = doctorService.getDoctorByDoctorId(doctorId);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);

        return repository.findByDoctorAndStatusAndStartsAtBetweenOrderByStartsAtAsc(
                doctor, PENDING, startOfDay, endOfDay
        );
    }

    public List<AppointmentTimeSlotResponse> getTimeSlotsFor(UUID doctorId, LocalDate date) {
        List<Appointment> appointments = getAppointmentsFor(doctorId, date);

        Set<LocalTime> bookedTimes = appointments.stream()
                .map(a -> a.getStartsAt().toLocalTime())
                .collect(Collectors.toSet());

        List<AppointmentTimeSlotResponse> result = new ArrayList<>();

        for (LocalTime time = START_TIME; !time.isAfter(END_TIME); time = time.plusMinutes(EXAMINATION_INTERVAL)) {

            boolean notInThePast = !date.isEqual(LocalDate.now()) || time.isAfter(LocalTime.now());
            boolean available = notInThePast && !bookedTimes.contains(time);

            result.add(new AppointmentTimeSlotResponse(time, available));
        }

        return result;
    }

    public List<Appointment> getPatientUpcomingAppointments(UUID uuid) {
        User user = userService.getUserById(uuid);
        return repository.findByPatientAndStatusInOrderByStartsAtAsc(user, List.of(PENDING, PROCESSING));
    }

    public List<Appointment> getPatientPastAppointments(UUID uuid) {
        User user = userService.getUserById(uuid);
        return repository.findByPatientAndStatusInOrderByStartsAtAsc(user, List.of(CANCELLED, COMPLETED, NO_SHOW));
    }

    public List<Appointment> getDoctorUpcomingAppointments(UUID uuid) {
        Doctor doctor = doctorService.getDoctorDetailsByUserId(uuid);
        return repository.findByDoctorAndStatusInOrderByStartsAtAsc(doctor, List.of(PENDING, PROCESSING));
    }

    public List<Appointment> getDoctorPastAppointments(UUID uuid) {
        Doctor doctor = doctorService.getDoctorDetailsByUserId(uuid);
        return repository.findByDoctorAndStatusInOrderByStartsAtAsc(doctor, List.of(CANCELLED, COMPLETED, NO_SHOW));
    }

    private void validateDateAndTime(LocalDate date, LocalTime time) {

        if (date.isBefore(LocalDate.now())) {
            throw new PastDateException();
        }

        if (time.isBefore(START_TIME) || time.isAfter(END_TIME)) {
            throw new InvalidTimeException();
        }

        int minutesFromStart = (int) Duration.between(START_TIME, time).toMinutes();
        if (minutesFromStart % EXAMINATION_INTERVAL != 0) {
            throw new InvalidTimeException();
        }
    }
}
