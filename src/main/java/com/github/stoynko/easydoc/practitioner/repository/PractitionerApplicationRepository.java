package com.github.stoynko.easydoc.practitioner.repository;

import com.github.stoynko.easydoc.practitioner.model.PractitionerApplication;

import java.util.List;
import java.util.UUID;

import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.practitioner.model.PractitionerApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PractitionerApplicationRepository extends JpaRepository<PractitionerApplication, UUID> {

    boolean existsPractitionerApplicationByUin(String uin);

    boolean existsPractitionerApplicationByUser(User user);

    int countPractitionerApplicationsByApplicationStatusIs(PractitionerApplicationStatus applicationStatus);

    List<PractitionerApplication> findAllByApplicationStatusIs(PractitionerApplicationStatus applicationStatus);

    boolean existsByUserIdAndApplicationStatus(UUID userId, PractitionerApplicationStatus applicationStatus);
}
