package com.github.stoynko.easydoc.shared.mapper;

import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.report.event.DraftReminderEvent;
import com.github.stoynko.easydoc.report.model.Report;
import com.github.stoynko.easydoc.user.model.User;
import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {

    public static DraftReminderEvent toDraftReminderEvent(Report report) {

        User patient = report.getAppointment().getPatient();
        Appointment appointment = report.getAppointment();
        DateTimeFormatter appointmentFormat = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' HH:mm");

        return DraftReminderEvent.builder()
                .recipient(patient.getEmailAddress())
                .patientName(patient.getFirstName() + " " + patient.getLastName())
                .atDate(appointmentFormat.format(appointment.getStartsAt()))
                .build();
    }
}
