package com.github.stoynko.easydoc.repositories;

import com.github.stoynko.easydoc.models.PractitionerApplication;

import java.util.List;
import java.util.UUID;

import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.models.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PractitionerApplicationRepository extends JpaRepository<PractitionerApplication, UUID> {

    boolean existsPractitionerApplicationByUin(String uin);

    boolean existsPractitionerApplicationByUser(User user);

    int countPractitionerApplicationsByApplicationStatusIs(ApplicationStatus applicationStatus);

    List<PractitionerApplication> findAllByApplicationStatusIs(ApplicationStatus applicationStatus);

    boolean existsByUserIdAndApplicationStatus(UUID userId, ApplicationStatus applicationStatus);
}
