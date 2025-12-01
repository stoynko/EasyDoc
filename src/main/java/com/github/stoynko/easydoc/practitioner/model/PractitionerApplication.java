package com.github.stoynko.easydoc.practitioner.model;

import com.github.stoynko.easydoc.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "practitioner_applications")
public class PractitionerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    private PractitionerApplicationStatus applicationStatus;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @Column(name = "uin", nullable = false, unique = true)
    private String uin;

    @Column(name = "profile_photo_url", nullable = false)
    private String profilePhotoUrl;

    @Column(name = "profile_photo_public_id", nullable = false)
    private String profilePhotoPublicId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "expertise", nullable = false)
    private Expertise expertise;

    @Column(name = "years_experience", nullable = false)
    private int yearsExperience;

    @Column(name = "practice_location",  nullable = false)
    private String practiceLocation;

    @Column(name = "professional_highlights", nullable = false, columnDefinition = "TEXT")
    private String professionalHighlights;

    @Column(name = "spoken_languages")
    private String spokenLanguages;
}
