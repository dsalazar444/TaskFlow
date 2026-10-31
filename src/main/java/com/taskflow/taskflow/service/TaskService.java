
package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.CreateTaskRequest;
import com.taskflow.taskflow.dto.TaskResponse;
import com.taskflow.taskflow.dto.UpdateTaskRequest;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import com.taskflow.taskflow.model.Task;
import com.taskflow.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // guarda en bd, y retorna el objeto Task -> No maneja lógica de HTTP, solo lógica de negocio
    public TaskResponse createTask(CreateTaskRequest request) {
        //save trabaja con task, entonces debemos hacer conversión
        Task task = fromCreateRequest(request);

        Task savedTask = taskRepository.save(task); // devuelve Task, si hya un error, lanza exception

        // objeto save se creó correctamente, y retornamos un DTOs, para controlar info que se manda
        return toResponse(savedTask);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponse) // la diferencia entre . y :: es la misma que entre fun y fun()
                .toList(); // si no encuentra, devuelve []
    }

    public TaskResponse getTaskById(Long id) {
        Task task = findTaskOrThrow(id);

        return toResponse(task);
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id)
            .ifPresent(taskRepository::delete); // para que sea idempotente -> no usamos getTaskById porque esta lanza exception, y queremos que no lo haga, pues es idempotente
            // ifPresent recibe un comportamiento que debe ejecutar si el Optional contiene un valor, por eso pasamos ::delete y no .delete
            // obtiene el task a eliminar de Optional, que se la entrega a ifPresent
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request){
        // UpdateTaskReq no tiene id, sino que lo tomamos de url -> pueden intentar IDOR -> authentication
        Task task = findTaskOrThrow(id);

        // si existe, actualizamos valores
        task.updateTask( // sin getters porque es un record
            request.title(),
            request.description(),
            request.dueDate(),
            request.status());
        
        Task updatedTask = taskRepository.save(task);

        return toResponse(updatedTask);
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate()
        );
    }

    private Task fromCreateRequest(CreateTaskRequest request) {
        return new Task(
            request.title(),
            request.description(),
            request.dueDate(),
            request.user(),
        );
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id) // findByid retorna un objeto tipo optional<Task>
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task", "id", id.toString()) 
                );
    }
}
