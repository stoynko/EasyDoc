package com.github.stoynko.easydoc.repositories;

import com.github.stoynko.easydoc.models.Doctor;
import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.models.enums.Expertise;
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
