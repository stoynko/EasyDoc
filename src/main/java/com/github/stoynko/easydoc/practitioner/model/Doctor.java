package com.github.stoynko.easydoc.practitioner.model;

import com.github.stoynko.easydoc.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
@EntityListeners(AuditingEntityListener.class)

public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
                foreignKey = @ForeignKey(name = "fk_doctor_user"))
    private User user;

    @Column(name = "profile_photo_url", nullable = false)
    private String profilePhotoUrl;

    @Column(name = "profile_photo_public_id", nullable = false)
    private String profilePhotoPublicId;

    @Column(name = "spoken_languages")
    private String spokenLanguages;

    @Column(name = "uin", nullable = false, unique = true)
    private String uin;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "expertise", nullable = false)
    private Expertise expertise;

    @Column(name = "years_experience", nullable = false)
    private int yearsExperience;

    @Column(name = "practice_location", nullable = false)
    private String practiceLocation;

    @Column(name = "professional_highlights", nullable = false, columnDefinition = "TEXT")
    private String professionalHighlights;

}
