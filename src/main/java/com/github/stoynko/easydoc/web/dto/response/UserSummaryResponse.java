package com.github.stoynko.easydoc.web.dto.response;

import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.models.enums.AccountStatus;
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

    private AccountRole accountRole;

    private AccountStatus accountStatus;

    private LocalDateTime creationDate;
}
