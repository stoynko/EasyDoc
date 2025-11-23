package com.github.stoynko.easydoc.models;

import com.github.stoynko.easydoc.models.embedded.CreatedModifiedAt;
import com.github.stoynko.easydoc.models.embedded.CreatedModifiedBy;
import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.models.enums.AccountStatus;
import com.github.stoynko.easydoc.models.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "personal_identification_number", unique = true)
    private String personalIdentificationNumber;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    private AccountRole role;

    @Column(name = "account_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus;

    @Embedded
    @Builder.Default
    private CreatedModifiedBy createdModifiedBy = new CreatedModifiedBy();

    @Embedded
    @Builder.Default
    private CreatedModifiedAt createdModifiedAt = new CreatedModifiedAt();
}
