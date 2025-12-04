package com.github.stoynko.easydoc.report.model;

import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.shared.embedded.CreatedModifiedAt;
import com.github.stoynko.easydoc.shared.embedded.CreatedModifiedBy;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "reports")
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "public_id", nullable = false, unique = true)
    private String publicId;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status")
    private ReportStatus reportStatus;

    @Column(name = "accompanying_illnesses", columnDefinition = "TEXT", length = 200)
    private String accompanyingIllnesses;

    @Column(name = "anamnesis", nullable = false, columnDefinition = "TEXT", length = 500)
    private String anamnesis;

    @Column(name = "status_at_exam", nullable = false, columnDefinition = "TEXT", length = 500)
    private String statusAtExam;

    @Column(name = "clinical_findings", columnDefinition = "TEXT", length = 1000)
    private String clinicalFindings;

    @Column(name = "care_recommendations", columnDefinition = "TEXT", length = 500)
    private String careRecommendations;

    @Column(name = "medicament_treatment", columnDefinition = "TEXT", length = 500)
    private String medicamentTreatment;

    @Column(name = "diagnosis", nullable = false, length = 100)
    private String diagnosis;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "draft_reminder_sent_at")
    private LocalDateTime draftReminderSentAt;

    @Embedded
    @Builder.Default
    private CreatedModifiedBy createdModifiedBy = new CreatedModifiedBy();

    @Embedded
    @Builder.Default
    private CreatedModifiedAt createdModifiedAt = new CreatedModifiedAt();
}
