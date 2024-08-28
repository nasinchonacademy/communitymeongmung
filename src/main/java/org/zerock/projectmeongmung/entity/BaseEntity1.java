package org.zerock.projectmeongmung.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Data
public abstract class BaseEntity1 {

    @CreatedDate
    @Column(name = "createddate", updatable = false)
    private LocalDateTime regdate; // LocalDateTime 사용

    @LastModifiedDate
    @Column(name = "modifieddate")
    private LocalDateTime modified; // LocalDateTime 사용

    @Column(name = "deleteddate")
    private LocalDateTime deleted; // LocalDateTime 사용
}
