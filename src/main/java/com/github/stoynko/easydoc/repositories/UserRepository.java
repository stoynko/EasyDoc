package com.github.stoynko.easydoc.repositories;

import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.models.enums.AccountRole;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsUserByPersonalIdentificationNumber(String pin);

    int countUserByRoleNot(AccountRole accountRole);

    int countByRole(AccountRole accountRole);

    boolean existsByEmailAddress(String emailAddress);

    Optional<User> findByEmailAddress(String emailAddress);

    List<User> findAllByRoleNot(AccountRole accountRole);

}
