package com.personal.quasar.controller;

import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.model.dto.TaskDTO;
import com.personal.quasar.model.entity.Task;
import com.personal.quasar.service.impl.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO task) throws InvalidFieldException {
        log.info("createTask is started with payload - {}", task);
        TaskDTO result = taskService.create(task);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable String id) {
        var task = taskService.get(id);

        return Optional.ofNullable(task)
                .map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/scheduled")
    public ResponseEntity<List<TaskDTO>> getTasksByDate(
            @DateTimeFormat(pattern = "dd-MM-yyyy")
            @RequestParam("date")
            Date date
    ) {
        log.info("getTasksByDate started with with date - {}", date);
        LocalDate localDate = date.toInstant()
                .atZone(ZoneId.of("Asia/Dhaka")) // Your timezone: +06
                .toLocalDate();
        Date utcDate = Date.from(localDate.atStartOfDay(ZoneId.of("UTC")).toInstant());
        return new ResponseEntity<>(taskService.getAllTasksByDate(utcDate), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable String id, @RequestBody TaskDTO updatedTask) throws InvalidFieldException {
        TaskDTO result = taskService.update(id, updatedTask);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}