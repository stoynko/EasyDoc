/*
package com.github.stoynko.hospital.models;

import com.github.stoynko.hospital.models.embedded.CreatedModifiedAt;
import com.github.stoynko.hospital.models.embedded.CreatedModifiedBy;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener.class)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_doctor_review"))
    private User user;

    @OneToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_user_review"))
    private Doctor doctor;

    @Column(name = "rating", nullable = false)
    private int rating;

    private String summary;

    @Embedded
    @Builder.Default
    private CreatedModifiedBy createdModifiedBy = new CreatedModifiedBy();

    @Embedded
    @Builder.Default
    private CreatedModifiedAt createdModifiedAt = new CreatedModifiedAt();
}
*/
