package com.github.stoynko.easydoc.services;

import com.github.stoynko.easydoc.events.UserContextRefreshEvent;
import com.github.stoynko.easydoc.exceptions.AlreadyOnboardedException;
import com.github.stoynko.easydoc.models.PractitionerApplication;
import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.repositories.PractitionerApplicationRepository;
import com.github.stoynko.easydoc.web.dto.request.RegisterPractitionerRequest;
import com.github.stoynko.easydoc.web.dto.response.CloudinaryUploadResult;
import com.github.stoynko.easydoc.web.mappers.EntityMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.ALREADY_ONBOARDED;
import static com.github.stoynko.easydoc.models.enums.AccountAuthority.CAN_SUBMIT_PRACTITIONER_APPLICATION;
import static com.github.stoynko.easydoc.models.enums.ApplicationStatus.APPROVED;
import static com.github.stoynko.easydoc.models.enums.ApplicationStatus.PENDING;
import static com.github.stoynko.easydoc.models.enums.ApplicationStatus.REJECTED;
import static com.github.stoynko.easydoc.web.mappers.DtoMapper.toChangePersonalDetailsFrom;

@Slf4j
@Service
public class PractitionerApplicationService {

    private final PractitionerApplicationRepository repository;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final DoctorService doctorService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public PractitionerApplicationService(PractitionerApplicationRepository repository, UserService userService, CloudinaryService cloudinaryService, DoctorService doctorService, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.doctorService = doctorService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void submitApplication(UUID uuid, RegisterPractitionerRequest request) throws IOException {

        User user = userService.getUserById(uuid);

        if (repository.existsPractitionerApplicationByUin(request.getUin())) {
            throw new AlreadyOnboardedException(ALREADY_ONBOARDED);
        }

        if (repository.existsPractitionerApplicationByUser(user)) {
            throw new AlreadyOnboardedException(ALREADY_ONBOARDED);
        }

        CloudinaryUploadResult uploadResult = cloudinaryService.uploadPhoto(request.getProfilePhoto(), "doctors");
        PractitionerApplication application = EntityMapper.toPractitionerApplicationEntity(user, request, uploadResult);

        repository.save(application);
        log.info("- applicationSubmission | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());

        userService.updatePersonalInfo(uuid, toChangePersonalDetailsFrom(request));
        userService.revokeAuthority(user.getId(), CAN_SUBMIT_PRACTITIONER_APPLICATION);
        eventPublisher.publishEvent(new UserContextRefreshEvent(user.getId()));
    }

    @Transactional
    public void approveApplication(UUID uuid) {

        PractitionerApplication application = getApplicationById(uuid);
        application.setApplicationStatus(APPROVED);
        repository.save(application);
        log.info("- applicationApproved | applicationId: {} timestamp:{}", application.getId(), LocalDateTime.now());

        doctorService.createDoctor(application);
    }

    @Transactional
    public void rejectApplication(UUID uuid) {
        PractitionerApplication application = getApplicationById(uuid);
        application.setApplicationStatus(REJECTED);
        repository.save(application);

        User user = application.getUser();
        userService.reinstateAuthority(user.getId(), CAN_SUBMIT_PRACTITIONER_APPLICATION);
        log.info("- applicationRejected | applicationId: {} timestamp:{}", application.getId(), LocalDateTime.now());
    }

    public PractitionerApplication getApplicationById(UUID uuid) {
        return repository.findById(uuid).orElseThrow(() -> new RuntimeException());
    }

    public List<PractitionerApplication> getAllPendingApplications() {
        return repository.findAllByApplicationStatusIs(PENDING);
    }

    public int getPendingApplicationsCount() {
        return repository.countPractitionerApplicationsByApplicationStatusIs(PENDING);
    }

    public boolean hasPendingApplication(UUID id) {
        return repository.existsByUserIdAndApplicationStatus(id, PENDING);
    }

    public boolean hasApprovedApplication(UUID id) {
        return repository.existsByUserIdAndApplicationStatus(id, APPROVED);
    }
}
