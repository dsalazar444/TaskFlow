
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

    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = fromCreateRequest(request);

        Task savedTask = taskRepository.save(task); // devuelve Task, si hya un error, lanza exception

        return toResponse(savedTask);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponse) 
                .toList();
    }

    public TaskResponse getTaskById(Long id) {
        Task task = findTaskOrThrow(id);

        return toResponse(task);
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id)
            .ifPresent(taskRepository::delete); 
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request){
        Task task = findTaskOrThrow(id);

        task.updateTask(
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
            request.dueDate()
        );
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id) 
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task", id) 
                );
    }
}
