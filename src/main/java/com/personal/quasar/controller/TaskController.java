package com.personal.quasar.controller;

import com.personal.quasar.dao.TaskRepository;
import com.personal.quasar.dto.ResponseMetaData;
import com.personal.quasar.entity.Task;
import com.personal.quasar.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/task")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST})
@Slf4j
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<ResponseMetaData> createTask(@RequestBody Task task) {
        var responseMetaData = new ResponseMetaData();
        responseMetaData.setId(taskService.create(task));
        responseMetaData.setMessage("Task created successfully");
        responseMetaData.setError(false);
        responseMetaData.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(responseMetaData, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        var task = taskService.get(id);

        return Optional.ofNullable(task)
                .map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMetaData> updateTask(@PathVariable String id, @RequestBody Task updatedTask) {
        var responseMetaData = new ResponseMetaData();
        responseMetaData.setId(taskService.update(id, updatedTask));
        responseMetaData.setMessage("Task updated successfully");
        responseMetaData.setError(false);
        responseMetaData.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(responseMetaData, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}