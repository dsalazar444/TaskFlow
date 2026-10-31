
package com.taskflow.taskflow.controller;

import com.taskflow.taskflow.model.Task;
import com.taskflow.taskflow.service.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // responsestatus es para especificar statuscode si funcion termina bien -> default: 200
    // si la funcion termina exitosamente, Spring lo toma como exitosa, y responde 200, en este caso, 201 porque se lo especificamos
    // si se lanza exception, el controller no llega a ejecutar el return pues la exception interrumpe la ejecución, entonces
    // entra el handler, y genera un response error
    public Task createTask(@RequestBody Task task) { // requestBody obtiene datos de json, y con ellos crea un objeto Task
        return taskService.createTask(task);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) 
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
