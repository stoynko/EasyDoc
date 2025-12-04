package com.github.stoynko.easydoc.user.model;

import com.github.stoynko.easydoc.shared.embedded.CreatedModifiedAt;
import com.github.stoynko.easydoc.shared.embedded.CreatedModifiedBy;
import com.github.stoynko.easydoc.user.model.AccountRole;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

import static com.github.stoynko.easydoc.user.model.AccountStatus.ACTIVE;

@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

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

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @UpdateTimestamp
    @Column(name = "email_verified_at", nullable = false)
    private LocalDateTime emailVerifiedAt;

    @Column(name = "profile_completed", nullable = false)
    private boolean profileCompleted;

    @Column(name = "account_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authorities")
    private Set<AccountAuthority> authority;

    @Embedded
    @Builder.Default
    private CreatedModifiedBy createdModifiedBy = new CreatedModifiedBy();

    @Embedded
    @Builder.Default
    private CreatedModifiedAt createdModifiedAt = new CreatedModifiedAt();

/*    public boolean isFullyActive() {
        return this.getAccountStatus() == ACTIVE && this.isProfileCompleted() && this.isEmailVerified();
    }*/
}
