package com.github.stoynko.easydoc.shared.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@Embeddable
@NoArgsConstructor
public class CreatedModifiedAt implements Serializable {

    @NotNull
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true)
    private LocalDateTime updatedAt;
}
