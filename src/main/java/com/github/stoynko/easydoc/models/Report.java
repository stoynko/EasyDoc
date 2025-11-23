package com.github.stoynko.easydoc.models;

import com.github.stoynko.easydoc.models.embedded.CreatedModifiedAt;
import com.github.stoynko.easydoc.models.embedded.CreatedModifiedBy;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

    @Column(name = "accompanying_illnesses", nullable = false)
    private String accompanyingIllnesses;

    @Column(name = "anamnesis")
    private String anamnesis;

    @Column(name = "status_at_exam")
    private String statusAtExam;

    @Column(name = "diagnostic_procedures")
    private String diagnosticProdecures;

    @Column(name = "recommendations")
    private String recommendations;

    private String prescription;

    @Embedded
    @Builder.Default
    private CreatedModifiedBy createdModifiedBy = new CreatedModifiedBy();

    @Embedded
    @Builder.Default
    private CreatedModifiedAt createdModifiedAt = new CreatedModifiedAt();
}
