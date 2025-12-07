package com.github.stoynko.easydoc.report.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DraftReminderEvent {

    private String recipient;

    private String patientName;

    private String atDate;
}
