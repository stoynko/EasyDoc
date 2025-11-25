package com.github.stoynko.easydoc.web.dto.response;

import com.github.stoynko.easydoc.models.enums.AccountAuthority;
import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.models.enums.AccountStatus;
import java.time.LocalDateTime;
import java.util.Set;
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

    private boolean canBookAppointment;
}
