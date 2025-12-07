package com.github.stoynko.easydoc.user.web.dto.response;

import com.github.stoynko.easydoc.user.model.AccountRole;
import com.github.stoynko.easydoc.user.model.AccountStatus;
import java.time.LocalDateTime;
import java.util.UUID;
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
public class UserSummaryResponse {

    private UUID id;

    private String name;

    private String emailAddress;

    private String profilePhotoUrl;

    private AccountRole accountRole;

    private AccountStatus accountStatus;

    private LocalDateTime creationDate;

    private LocalDateTime updatedDate;

    private boolean canBookAppointment;
}
