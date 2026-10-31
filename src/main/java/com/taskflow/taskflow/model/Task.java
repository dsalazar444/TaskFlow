package com.taskflow.taskflow.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "task")
public class Task extends BaseEntity{

    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT") // esto de = text evita que tengamos que nponer length, pues sindica que será texto de longitud variable
    private String description;

    @Enumerated(EnumType.STRING) //@.. indica como se almacenará valor en bd -> con .string indicamos que se almacene texto, y no como index de lista
    @Column(name = "status", nullable = false, length = 20)
    private TaskStatus status = TaskStatus.PENDING; // valor por default

    @Column(name = "due_date")
    private LocalDate dueDate;

    // EXTENDED TASK ATTRIBUTES
    // private LocalDateTime modifiedAt
    // private LocalDateTime createdAt

    protected Task() {
        // required constructor for JPA.
        // JPA necesita este constructor para cuando trae registros de la BD -> debe crear
        // objetos Task con esto, entonces obliga crearle un constructor vacio para él.
        // es vacio porque Hibernate necesita poder crear objeto antes de asignarle valores,
        // porque RECUPERA, no CREA. El otro es para crear, no para recuperar.
    }

    public Task(String title, String description, LocalDate dueDate) {
        setTitle(title); // dentro del setter se válida, y se asigna. Para que siempre se creen objetos validos
        this.description = description;
        this.dueDate = dueDate;
        this.status = TaskStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        if (title == null || title.isBlank()){
            throw new IllegalArgumentException("Title cannot be blank nor null");

        }

        if (!Objects.equals(this.title, title)) {
            this.title = title;
        }
    }
    

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        if (!Objects.equals(this.description, description)) {
            this.description = description;
        }
    }

    public TaskStatus getStatus() {
        return status;
    }
    public void setStatus(TaskStatus status) {
        if (!Objects.equals(this.status, status)) {
            this.status = status;
        }
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        if (!Objects.equals(this.dueDate, dueDate)) {
            this.dueDate = dueDate;
        }
    }

    // EXTENDED TASK METHODS 
    // public LocalDateTime getCreatedAt() 
    // public LocalDateTime getModifiedAt()

    public void updateTask(
            String title,
            String description,
            LocalDate dueDate,
            TaskStatus status
    ) {
        setTitle(title);
        setDescription(description);
        setDueDate(dueDate);
        setStatus(status);
    }

}
