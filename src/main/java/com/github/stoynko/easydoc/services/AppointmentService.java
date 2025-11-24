package com.github.stoynko.easydoc.services;

import com.github.stoynko.easydoc.exceptions.AppointmentDoesNotExistException;
import com.github.stoynko.easydoc.exceptions.ErrorMessages;
import com.github.stoynko.easydoc.exceptions.InvalidTimeException;
import com.github.stoynko.easydoc.exceptions.PastDateException;
import com.github.stoynko.easydoc.models.Appointment;
import com.github.stoynko.easydoc.models.Doctor;
import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.repositories.AppointmentRepository;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.dto.request.AppointmentRequest;

import com.github.stoynko.easydoc.web.dto.response.AppointmentTimeSlotResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.APPOINTMENT_DATE_INVALID;
import static com.github.stoynko.easydoc.exceptions.ErrorMessages.APPOINTMENT_TIME_INVALID;
import static com.github.stoynko.easydoc.exceptions.ErrorMessages.APPOINTMENT_TIME_OUTSIDE_HOURS;
import static com.github.stoynko.easydoc.models.enums.AppointmentStatus.CANCELLED;
import static com.github.stoynko.easydoc.models.enums.AppointmentStatus.COMPLETED;
import static com.github.stoynko.easydoc.models.enums.AppointmentStatus.CONFIRMED;
import static com.github.stoynko.easydoc.models.enums.AppointmentStatus.NO_SHOW;
import static com.github.stoynko.easydoc.utilities.GenerationalUtilities.extractDigits;

@Slf4j
@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final DoctorService doctorService;
    private final UserService userService;

    private static final LocalTime START_TIME = LocalTime.of(8, 0);
    private static final LocalTime END_TIME = LocalTime.of(15, 40);
    private static final int EXAMINATION_INTERVAL = 20;

    @Autowired
    public AppointmentService(AppointmentRepository repository, DoctorService doctorService, UserService userService) {
        this.repository = repository;
        this.doctorService = doctorService;
        this.userService = userService;
    }

    public void createAppointment(UUID patientId, UUID doctorId, AppointmentRequest request) {

        validateDateAndTime(request.getDate(), request.getTime());

        /*TODO: IF DOCTOR ALREADY HAS BOOKED APPOINTMENT FOR THAT DATE AND TIME THROW ERROR
        if (repository.existsAppointmentByDoctorAnd) {
            throw new AppointmentAlreadyExists();
        }*/

        /* TODO: IF PATIENT ALREADY HAS BOOKED APPOINTMENT FOR THAT DATE THROW ERROR
        if (repository.existsAppointmentByPatientAndDoctorAndDate()) {
            throw new AppointmentAlreadyExistsException();
        }*/

        Appointment appointment = Appointment.builder()
                .publicId(extractDigits(UUID.randomUUID().toString()))
                .patient(userService.getUserById(patientId))
                .doctor(doctorService.getDoctorByDoctorId(doctorId))
                .startsAt(LocalDateTime.of(request.getDate(), request.getTime()))
                .appointmentReason(request.getAppointmentReason())
                .additionalNotes(request.getAdditionalNotes())
                .status(CONFIRMED)
                .build();

        repository.save(appointment);
    }

    public List<Appointment> getPatientUpcomingAppointments(UUID uuid) {
        User user = userService.getUserById(uuid);
        return repository.findByPatientAndStatusInOrderByStartsAtAsc(user, List.of(CONFIRMED));
    }

    public List<Appointment> getPatientPastAppointments(UUID uuid) {
        User user = userService.getUserById(uuid);
        return repository.findByPatientAndStatusInOrderByStartsAtAsc(user, List.of(CANCELLED, COMPLETED, NO_SHOW));
    }

    public List<Appointment> getDoctorUpcomingAppointments(UUID uuid) {
        Doctor doctor = doctorService.getDoctorDetailsByUserId(uuid);
        return repository.findByDoctorAndStatusInOrderByStartsAtAsc(doctor, List.of(CONFIRMED));
    }

    public List<Appointment> getDoctorPastAppointments(UUID uuid) {
        Doctor doctor = doctorService.getDoctorDetailsByUserId(uuid);
        return repository.findByDoctorAndStatusInOrderByStartsAtAsc(doctor, List.of(CANCELLED, COMPLETED, NO_SHOW));
    }

    private void validateDateAndTime(LocalDate date, LocalTime time) {

        if (date.isBefore(LocalDate.now())) {
            throw new PastDateException(APPOINTMENT_DATE_INVALID);
        }

        if (time.isBefore(START_TIME) || time.isAfter(END_TIME)) {
            throw new InvalidTimeException(APPOINTMENT_TIME_OUTSIDE_HOURS);
        }

        int minutesFromStart = (int) Duration.between(START_TIME, time).toMinutes();
        if (minutesFromStart % EXAMINATION_INTERVAL != 0) {
            throw new InvalidTimeException(APPOINTMENT_TIME_INVALID);
        }
    }

    public List<Appointment> getAppointmentsFor(UUID doctorId, LocalDate date) {
        Doctor doctor = doctorService.getDoctorByDoctorId(doctorId);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay   = date.plusDays(1).atStartOfDay().minusNanos(1);

        return repository.findByDoctorAndStatusAndStartsAtBetweenOrderByStartsAtAsc(
                doctor, CONFIRMED, startOfDay, endOfDay
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

    public void cancelAppointment(UUID appointmentId, UserAuthenticationDetails principal) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(CANCELLED);
        repository.save(appointment);
        log.info("-appointmentCancellation | appointment:{} timestamp:{}", appointment.getId(),  LocalDateTime.now());
    }

    private Appointment getAppointmentById(UUID id) {
        return repository.findAppointmentById(id)
                .orElseThrow(() -> new AppointmentDoesNotExistException(ErrorMessages.APPOINTMENT_NOT_FOUND));

    }

    public void markAsNoShow(UUID appointmentId, UserAuthenticationDetails principal) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(NO_SHOW);
        repository.save(appointment);
        log.info("-appointmentNoShow | appointment: {} timestamp: {}", appointment.getId(),  LocalDateTime.now());
    }

    /*public List<Appointment> getUpcomingAppointments(UUID doctorUserId) {
        Doctor doctor = doctorService.getDoctorById(doctorUserId);
        LocalDateTime now = LocalDateTime.
        List<Appointment> appointments = repository.
                findByDoctorAndStatusAndStartsAtGreaterThanEqualOrderByStartsAtAsc(doctor, CONFIRMED, now);
    }*/
}
