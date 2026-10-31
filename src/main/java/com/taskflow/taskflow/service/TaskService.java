
package com.taskflow.taskflow.service;

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
    public Task createTask(Task task) {
        return taskRepository.save(task); // devuelve Task, si hya un error, lanza exception
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll(); // si no encuentra, devuelve []
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id) // findByid retorna un objeto tipo optional<Task>
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task", id)
                );
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id)
            .ifPresent(taskRepository::delete); // para que sea idempotente -> no usamos getTaskById porque esta lanza exception, y queremos que no lo haga, pues es idempotente
    }
}
