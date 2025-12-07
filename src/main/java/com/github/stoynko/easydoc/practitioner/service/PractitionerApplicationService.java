package com.github.stoynko.easydoc.practitioner.service;

import com.github.stoynko.easydoc.media.dto.CloudinaryUploadResult;
import com.github.stoynko.easydoc.media.service.CloudinaryService;
import com.github.stoynko.easydoc.practitioner.exception.PractitionerAlreadyOnboardedException;
import com.github.stoynko.easydoc.practitioner.model.PractitionerApplication;
import com.github.stoynko.easydoc.practitioner.repository.PractitionerApplicationRepository;
import com.github.stoynko.easydoc.practitioner.web.dto.request.RegisterPractitionerRequest;
import com.github.stoynko.easydoc.shared.event.UserContextRefreshEvent;
import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.github.stoynko.easydoc.practitioner.model.PractitionerApplicationStatus.APPROVED;
import static com.github.stoynko.easydoc.practitioner.model.PractitionerApplicationStatus.PENDING;
import static com.github.stoynko.easydoc.practitioner.model.PractitionerApplicationStatus.REJECTED;
import static com.github.stoynko.easydoc.practitioner.web.mapper.PractitionerMapper.toPractitionerApplicationEntity;
import static com.github.stoynko.easydoc.user.model.AccountAuthority.CAN_SUBMIT_PRACTITIONER_APPLICATION;
import static com.github.stoynko.easydoc.user.web.mapper.UserMapper.toChangePersonalDetailsFrom;

@Slf4j
@Service
@RequiredArgsConstructor
public class PractitionerApplicationService {

    private final PractitionerApplicationRepository repository;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final DoctorService doctorService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void submitApplication(UUID uuid, RegisterPractitionerRequest request) throws IOException {

        User user = userService.getUserById(uuid);

        if (repository.existsPractitionerApplicationByUin(request.getUin())) {
            throw new PractitionerAlreadyOnboardedException();
        }

        if (repository.existsPractitionerApplicationByUser(user)) {
            throw new PractitionerAlreadyOnboardedException();
        }

        CloudinaryUploadResult uploadResult = cloudinaryService.uploadPhoto(request.getProfilePhoto(), "doctors");
        PractitionerApplication application = toPractitionerApplicationEntity(user, request, uploadResult);

        repository.save(application);
        log.info("[practitioner-application-submitted] User {} submitted application for practitioner at {}", user.getId(), LocalDateTime.now());

        userService.updatePersonalInfo(uuid, toChangePersonalDetailsFrom(request));
        userService.revokeAuthority(user.getId(), CAN_SUBMIT_PRACTITIONER_APPLICATION);
        eventPublisher.publishEvent(new UserContextRefreshEvent(user.getId()));
    }

    @Transactional
    public void approveApplication(UUID uuid) {

        PractitionerApplication application = getApplicationById(uuid);
        application.setApplicationStatus(APPROVED);
        repository.save(application);
        log.info("[practitioner-application-approved] | Practitioner application {} was approved at {}", application.getId(), LocalDateTime.now());

        doctorService.createDoctor(application);
    }

    @Transactional
    public void rejectApplication(UUID uuid) {
        PractitionerApplication application = getApplicationById(uuid);
        application.setApplicationStatus(REJECTED);
        repository.save(application);

        User user = application.getUser();
        userService.reinstateAuthority(user.getId(), CAN_SUBMIT_PRACTITIONER_APPLICATION);
        log.info("[practitioner-application-rejected] | Practitioner application {} was rejected at {}", application.getId(), LocalDateTime.now());
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
