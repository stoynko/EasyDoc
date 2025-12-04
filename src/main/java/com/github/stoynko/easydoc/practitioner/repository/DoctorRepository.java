package com.github.stoynko.easydoc.practitioner.repository;

import com.github.stoynko.easydoc.practitioner.model.Doctor;
import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.practitioner.model.Expertise;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<Doctor> findByUserId(UUID id);

    boolean existsDoctorByUin(String uin);

    boolean existsDoctorByUser(User user);

    List<Doctor> findAllByExpertise(Expertise expertise);
}
