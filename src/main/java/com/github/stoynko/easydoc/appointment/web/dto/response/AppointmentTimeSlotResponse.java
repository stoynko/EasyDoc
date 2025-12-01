package com.github.stoynko.easydoc.appointment.web.dto.response;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@AllArgsConstructor
public class AppointmentTimeSlotResponse {

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    private boolean isAvailable;
}
