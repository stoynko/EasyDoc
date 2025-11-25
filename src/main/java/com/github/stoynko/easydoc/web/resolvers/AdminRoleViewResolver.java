package com.github.stoynko.easydoc.web.resolvers;

import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.services.PractitionerApplicationService;
import com.github.stoynko.easydoc.services.UserService;
import com.github.stoynko.easydoc.web.dto.DtoAggregator;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.mappers.DtoMapper;
import com.github.stoynko.easydoc.web.model.ViewFragment;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.github.stoynko.easydoc.models.enums.AccountRole.ADMIN;

@Component
public class AdminRoleViewResolver implements RoleViewResolver{

    private final DtoAggregator dtoAggregator;
    private final UserService userService;
    private final PractitionerApplicationService practitionerApplicationService;

    @Autowired
    public AdminRoleViewResolver(DtoAggregator dtoAggregator, UserService userService, PractitionerApplicationService practitionerApplicationService) {
        this.dtoAggregator = dtoAggregator;
        this.userService = userService;
        this.practitionerApplicationService = practitionerApplicationService;
    }

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
                                                .stream().map(DtoMapper::toUserSummaryFrom)
                                                .collect(Collectors.toList()));

            case PRACTITIONER_APPLICATIONS -> model.put("applications",
                                                practitionerApplicationService.getAllPendingApplications()
                                                .stream().map(DtoMapper::toPendingPractitionerApplicationFrom)
                                                .collect(Collectors.toList()));

            case SETTINGS -> {
                ViewFragment viewFragment = dtoContext.viewFragment();
                dtoAggregator.aggregateDtoForFragment(model, viewFragment, dtoContext.principal().getId());
            }

        }
            model.put("userSummary", DtoMapper.toUserSummary(userService.getUserById(dtoContext.principal().getId())));
        return model;
    }
}
