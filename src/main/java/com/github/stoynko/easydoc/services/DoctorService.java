package com.github.stoynko.easydoc.services;

import com.github.stoynko.easydoc.exceptions.DoctorNotFound;
import com.github.stoynko.easydoc.models.Doctor;
import com.github.stoynko.easydoc.models.PractitionerApplication;
import com.github.stoynko.easydoc.models.enums.Expertise;
import com.github.stoynko.easydoc.repositories.DoctorRepository;
import com.github.stoynko.easydoc.security.SecurityContextUpdater;
import com.github.stoynko.easydoc.web.dto.request.*;
import com.github.stoynko.easydoc.web.dto.response.CloudinaryUploadResult;
import com.github.stoynko.easydoc.web.dto.response.DoctorBriefSummaryResponse;
import com.github.stoynko.easydoc.web.mappers.DtoMapper;
import com.github.stoynko.easydoc.web.mappers.EntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.stoynko.easydoc.models.enums.AccountRole.DOCTOR;

@Slf4j
@Service
public class DoctorService {

    private final DoctorRepository repository;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final SecurityContextUpdater contextUpdater;

    @Autowired
    public DoctorService(DoctorRepository repository, UserService userService, CloudinaryService cloudinaryService, SecurityContextUpdater contextUpdater) {
        this.repository = repository;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.contextUpdater = contextUpdater;
    }

    public Doctor getDoctorByDoctorId(UUID uuid) {
        return repository.findById(uuid).orElseThrow(() -> new DoctorNotFound());
    }

    public Doctor getDoctorDetailsByUserId(UUID uuid) {
        return repository.findByUserId(uuid).orElseThrow(() -> new RuntimeException("User with id: " + uuid + "is not associated with doctor's account."));
    }

    public List<DoctorBriefSummaryResponse> getAllDoctors(){
        return repository.findAll().stream()
                .map(DtoMapper::toDoctorBriefInfoFrom)
                .collect(Collectors.toList());
    }

    public List<DoctorBriefSummaryResponse> getDoctorsByExpertise(Expertise expertise) {
        return repository.findAllByExpertise(expertise).stream()
                .map(DtoMapper::toDoctorBriefInfoFrom)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserProfilePhoto(UUID uuid, UpdateProfilePhotoRequest request) throws IOException {
        Doctor doctor = getDoctorDetailsByUserId(uuid);
        if (doctor.getProfilePhotoUrl() != null) {
            cloudinaryService.deletePhoto(doctor.getProfilePhotoPublicId());
        }

        CloudinaryUploadResult uploadResult = cloudinaryService.uploadPhoto(request.getFileUpload(), "doctors");

        doctor.setProfilePhotoUrl(uploadResult.url());
        doctor.setProfilePhotoPublicId(uploadResult.publicId());
        repository.save(doctor);
    }

    public void changeProfessionalDetails(UUID uuid, UpdateProfessionalDetailsRequest request) {

        Doctor doctor = getDoctorDetailsByUserId(uuid);
        doctor.setExpertise(request.getExpertise());
        doctor.setPracticeLocation(request.getPracticeLocation());
        doctor.setYearsExperience(request.getYearsExperience());
        doctor.setProfessionalHighlights(request.getProfessionalHighlights());
        doctor.setSpokenLanguages(request.getSpokenLanguages());
        repository.save(doctor);
        log.info("- update-professional-info | userId: {} timestamp:{}", uuid, LocalDateTime.now());
    }

    @Transactional
    public void createDoctor(PractitionerApplication application) {

        Doctor doctor = EntityMapper.toDoctorEntity(application);
        repository.save(doctor);
        log.info("-doctorOnboarding | userId: {} timestamp:{}", application.getUser().getId(), LocalDateTime.now());

        userService.updateUserRoleTo(application.getUser(), DOCTOR);
    }
}
