package com.github.stoynko.easydoc.user.repository;

import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.user.model.AccountRole;
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

    List<User> findAllByRole(AccountRole role);
}
