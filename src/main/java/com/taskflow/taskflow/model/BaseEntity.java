package com.taskflow.taskflow.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass // Indica que la clase no está asociada a una tabla, sino que sus atributos se incorporan al mapeo de las entidades que hereden de ella
@EntityListeners(AuditingEntityListener.class) // registra un listener, el cual nos permite escuchar determinados eventos en el ciclo de vida de una entidad.
// AuditaEnt.. escucha eventos @PerPersist y @PreUpdate

// Con @EntityListeners conectamos nuestras entidades con un listener
// que escucha determinados eventos del ciclo de vida de JPA.

// AuditingEntityListener conecta esos eventos con el mecanismo
// de auditoría de Spring Data JPA (que es el que se activa con @EnableJpaAuditing)

// Finalmente, @CreatedDate y @LastModifiedDate indican qué atributos
// deben actualizarse automáticamente con los timestamps correspondientes
public abstract class BaseEntity {

    @CreatedDate // encargado de registrar fecha automaticamente
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}