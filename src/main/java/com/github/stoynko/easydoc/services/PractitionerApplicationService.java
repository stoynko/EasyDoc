package com.github.stoynko.easydoc.services;

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
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.ALREADY_ONBOARDED;
import static com.github.stoynko.easydoc.models.enums.ApplicationStatus.APPROVED;
import static com.github.stoynko.easydoc.models.enums.ApplicationStatus.PENDING;
import static com.github.stoynko.easydoc.models.enums.ApplicationStatus.REJECTED;
import static com.github.stoynko.easydoc.web.mappers.DtoMapper.getChangePersonalDetailsFrom;

@Slf4j
@Service
public class PractitionerApplicationService {

    private final PractitionerApplicationRepository repository;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final DoctorService doctorService;

    @Autowired
    public PractitionerApplicationService(PractitionerApplicationRepository repository, UserService userService, CloudinaryService cloudinaryService, DoctorService doctorService) {
        this.repository = repository;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.doctorService = doctorService;
    }

    public PractitionerApplication getApplicationById(UUID uuid) {
        Optional<PractitionerApplication> optionalApplication = repository.findById(uuid);

        if (optionalApplication.isEmpty()) {
            //TODO: ADD CUSTOM EXCEPTION
            throw new RuntimeException();
        }

        return optionalApplication.get();
    }

    @Transactional
    public void approveApplication(UUID uuid) {

        PractitionerApplication application = getApplicationById(uuid);
        application.setApplicationStatus(APPROVED);
        repository.save(application);
        log.info("- applicationApproved | applicationId: {} timestamp:{}", application.getId(), LocalDateTime.now());

        doctorService.createDoctor(application);
    }

    public void rejectApplication(UUID uuid) {
        PractitionerApplication application = getApplicationById(uuid);
        application.setApplicationStatus(REJECTED);
        repository.save(application);
        log.info("- applicationRejected | applicationId: {} timestamp:{}", application.getId(), LocalDateTime.now());
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

        userService.updatePersonalInfo(uuid, getChangePersonalDetailsFrom(request));
    }

    public List<PractitionerApplication> getAllPendingApplications() {
        return repository.findAllByApplicationStatusIs(PENDING);
    }

    public int getPendingApplicationsCount() {
        return repository.countPractitionerApplicationsByApplicationStatusIs(PENDING);
    }

    public boolean hasSubmittedApplication(UUID id) {
        return repository.existsByUserIdAndApplicationStatus(id, PENDING);
    }

    //log.info("- roleChange | userId: {} timestamp:{}", user.getId(), Instant.now());
    //userService.updateUserRoleTo(user, DOCTOR);
    //contextUpdater.refreshContext(user.getEmailAddress());
}
