package com.github.stoynko.easydoc.web.resolvers;

import com.github.stoynko.easydoc.practitioner.service.PractitionerApplicationService;
import com.github.stoynko.easydoc.practitioner.web.mapper.PractitionerMapper;
import com.github.stoynko.easydoc.user.model.AccountRole;
import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.user.service.UserService;
import com.github.stoynko.easydoc.user.web.mapper.UserMapper;
import com.github.stoynko.easydoc.web.dto.DtoAggregator;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.model.ViewFragment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.stoynko.easydoc.user.model.AccountRole.ADMIN;
import static com.github.stoynko.easydoc.user.web.mapper.UserMapper.toUserSummary;

@Component
@RequiredArgsConstructor
public class AdminRoleViewResolver implements RoleViewResolver {

    private final DtoAggregator dtoAggregator;
    private final UserService userService;
    private final PractitionerApplicationService practitionerApplicationService;

    @Override
    public AccountRole getSupportedRole() {
        return ADMIN;
    }

    @Override
    public Map<String, Object> buildModelData(DtoContext dtoContext) {

        Map<String, Object> model = new HashMap<>();

        switch (dtoContext.viewPage()) {

            case DASHBOARD -> {
                model.put("usersCountExceptAdmins", userService.getTotalUsersCountExceptAdmins());
                model.put("doctorsCount", userService.getDoctorsCount());
                model.put("pendingApplicationsCount", practitionerApplicationService.getPendingApplicationsCount());
            }

            case USERS -> model.put("users", userService.getAllUsersExceptAdmins()
                    .stream().map(UserMapper::toUserSummaryFrom)
                    .collect(Collectors.toList()));

            case PRACTITIONER_APPLICATIONS -> model.put("applications",

                    practitionerApplicationService.getAllPendingApplications()
                            .stream().map(PractitionerMapper::toPendingPractitionerApplicationFrom)
                            .collect(Collectors.toList()));

            case SETTINGS -> {
                ViewFragment viewFragment = dtoContext.viewFragment();
                dtoAggregator.aggregateDtoForFragment(model, viewFragment, dtoContext.principal().getId());
            }

        }

        if (dtoContext.principal() != null) {

            User user = userService.getUserById(dtoContext.principal().getId());

            model.put("userSummary", toUserSummary(userService.getUserById(dtoContext.principal().getId())));
            model.put("isProfileCompleted", user.isProfileCompleted());
        }

        return model;
    }
}
