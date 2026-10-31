
package com.taskflow.taskflow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // EXTENDED TASK ATTRIBUTES
    // private LocalDateTime modifiedAt
    // private LocalDateTime createdAt


    protected User() {
        // Constructor requerido por JPA.
    }

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // EXTENDED TASK METHODS 
    // public LocalDateTime getCreatedAt() 
    // public LocalDateTime getModifiedAt()

}
