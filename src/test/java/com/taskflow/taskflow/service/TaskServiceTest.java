package com.taskflow.taskflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskflow.taskflow.dto.CreateTaskRequest;
import com.taskflow.taskflow.dto.TaskResponse;
import com.taskflow.taskflow.dto.UpdateTaskRequest;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import com.taskflow.taskflow.repository.TaskRepository;
import com.taskflow.taskflow.model.Task;

@ExtendWith(MockitoExtension.class)
// Las extensiones son componentes que JUnit puede invocar
// en determinados momentos de su ciclo de vida.

// ¿Por qué existen?
// Porque permiten incorporar comportamientos adicionales
// sin modificar el funcionamiento interno de JUnit.

// Mockito tiene sus propias anotaciones (@Mock, @InjectMocks),
// pero JUnit no conoce cómo procesarlas.

// Por eso registramos MockitoExtension mediante @ExtendWith.

// MockitoExtension.class es la clase que implementa esa extensión.

// Cuando JUnit ejecuta las pruebas:
// 1. Invoca a MockitoExtension en los puntos correspondientes.
// 2. MockitoExtension procesa las anotaciones de Mockito.
// 3. Crea los mocks e inyecta las dependencias.
// 4. JUnit continúa con la ejecución de nuestros tests.
class TaskServiceTest {

    @Mock // Creamos mock de repo -> genera respuesta default segun el tipo de retorno -> 
    // podemos configurar las respuestas utilizando when() y thenReturn().
    private TaskRepository taskRepository;

    @InjectMocks // crea service real, y le inyecta mocks de dependencia
    private TaskService taskService;


    // Test "CreateTask()"
    @Test
    void shouldCreateTaskSuccessfully() {

        // Arrange
        CreateTaskRequest request = new CreateTaskRequest(
            "Learn JUnit",
            "Study unit testing with JUnit and Mockito",
            LocalDate.of(2026, 11, 15)
        );

        // definimos comportamiento para cuando se llame funcion de repository (en createTask, por ejm)
        // si no la pusieramos, igual no se llamaría a bd, porque taskRepository sigue siendo
        // un mock. Mockito no ejecuta la implementación real de save(). Si no configuramos
        // su comportamiento, devolverá el valor predeterminado para el tipo de retorno: en este caso, null.
        when(taskRepository.save(any(Task.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
            // invocation es un objeto que representa la invocación del método simulado. Mockito lo proporciona cuando se ejecuta el thenAnswer()
            // uno de sus elementos, es justo el objeto que recibe

        // Act
        TaskResponse response = taskService.createTask(request); // aqui se usa la nueva definicion de save 
        // -> taskresponse viene con id null

        // Assert
        assertEquals(request.title(), response.title());
        assertEquals(request.description(), response.description());
        assertEquals(request.dueDate(), response.dueDate());
        assertEquals(Task.TaskStatus.PENDING, response.status());

        // taskCaptor -> Nos permite capturar los argumentos de cierto tipo que recibe un mock. -> para save() por ejm
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        // Aquí ocurren dos cosas:
        // 1. verify() comprueba que save() fue invocado.
        // 2. capture() captura el objeto que recibió.
        // Verify no vuelve a ejecutar el metodo, solo busca que se haya llamado save (mockito registra
        // llamadas por default) -> si sí, obtiene el argumento que recibió, del tipo del captor, que en nuestro caso es Task
        verify(taskRepository).save(taskCaptor.capture());

        Task savedTask = taskCaptor.getValue(); // obtenemos la instancia cpaturada

        // verificamos que coincidan
        assertEquals(request.title(), savedTask.getTitle());
        assertEquals(request.description(), savedTask.getDescription());
        assertEquals(request.dueDate(), savedTask.getDueDate());
    }

    // Test "getAllTasks()"
    @Test
    void shouldReturnAllTasks() {

        // Arrange
        Task task1 = new Task(
            "Learn crochet",
            "knit a stuffed toy",
            LocalDate.of(2026, 11, 15)
        );

        Task task2 = new Task(
            "Learn Mockito",
            "Study mocks",
            LocalDate.of(2026, 11, 20)
        );

        when(taskRepository.findAll())
            .thenReturn(List.of(task1, task2)); // son datos "quemados" por eso se usna return en lugar de response

        // Act
        List<TaskResponse> response = taskService.getAllTasks();

        // Assert
        assertEquals(2, response.size());

        assertEquals("Learn crochet", response.get(0).title());
        assertEquals("Learn Mockito", response.get(1).title());

        verify(taskRepository).findAll(); // verificamos que si se haya llamado
    }

    @Test
    void shouldReturnEmptyListWhenNoTasksExist() {

        // Arrange
        when(taskRepository.findAll())
            .thenReturn(List.of());

        // Act
        List<TaskResponse> response = taskService.getAllTasks();

        // Assert
        assertTrue(response.isEmpty());

        verify(taskRepository).findAll();
    }

    // Test "getTaskById()"
    @Test
    void shouldReturnTaskById() {

        // Arrange
        Long taskId = 1L;

        Task task = new Task(
            "Learn JUnit",
            "Study testing",
            LocalDate.of(2026, 11, 15)
        );

        when(taskRepository.findById(taskId))
            .thenReturn(Optional.of(task));

        // Act
        TaskResponse response = taskService.getTaskById(taskId);

        // Assert
        assertNotNull(response);
        assertEquals(task.getTitle(), response.title());
        assertEquals(task.getDescription(), response.description());
        assertEquals(task.getDueDate(), response.dueDate());
        assertEquals(task.getStatus(), response.status());

        verify(taskRepository).findById(taskId);
    }

    @Test
    void shouldThrowExceptionWhenTaskDoesNotExist() {

        // Arrange
        Long taskId = 99L;

        when(taskRepository.findById(taskId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows( // heramienta que nos permite verificar que la excepcion que se lanza, si es la esperada -> por eso act & assert se hacen juntos
            ResourceNotFoundException.class,
            () -> taskService.getTaskById(taskId)
        );

        verify(taskRepository).findById(taskId);
    }

    // Test "deleteTask()"
    @Test
    void shouldDeleteTaskWhenItExists() {

        // Arrange
        Long taskId = 1L;

        Task task = new Task(
            "Learn JUnit",
            "Study testing",
            LocalDate.now()
        );

        when(taskRepository.findById(taskId))
            .thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task);
    }

    void shouldNotDeleteAnythingWhenTaskDoesNotExist() {

        // Arrange
        Long taskId = 50L;

        when(taskRepository.findById(taskId))
            .thenReturn(Optional.empty());

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).delete(any(Task.class));

    }

    // Test "updateTask()"
    @Test
    void shouldUpdateTaskSuccessfully() {

        // Arrange
        Long taskId = 1L;

        // task a actualizar -> la que deberia estar en bd
        Task task = new Task(
            "Learn JUnit",
            "Old description",
            LocalDate.of(2026, 11, 15)
        );

        UpdateTaskRequest request = new UpdateTaskRequest(
            "Learn JUnit", // si es igual a como estaba, no se llama a bd porque update se hace con setters
            "New description",
            LocalDate.of(2026, 12, 1),
            Task.TaskStatus.IN_PROGRESS
        );

        when(taskRepository.findById(taskId))
            .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskResponse response = taskService.updateTask(taskId, request);

        // Assert
        assertEquals(request.title(), response.title());
        assertEquals(request.description(), response.description());
        assertEquals(request.dueDate(), response.dueDate());
        assertEquals(request.status(), response.status());

        verify(taskRepository).findById(taskId);
        // verificamos sobre task, porque update obtiene task con ese id, y sobre ESE elemento, modifica
        // entonces cuando se hace save, se hace sobre la misma entidad
        verify(taskRepository).save(task);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentTask() {

        // Arrange
        Long taskId = 99L;

        UpdateTaskRequest request = new UpdateTaskRequest(
            "New title",
            "New description",
            LocalDate.of(2026, 12, 1),
            Task.TaskStatus.COMPLETED
        );

        when(taskRepository.findById(taskId))
            .thenReturn(Optional.empty());
        // no hay que definir .save() porque nunca llega hasta allá

        // Act & Assert
        assertThrows(
            ResourceNotFoundException.class,
            () -> taskService.updateTask(taskId, request)
        );

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }
}