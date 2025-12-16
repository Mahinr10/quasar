package com.personal.quasar.service.impl;

import com.personal.quasar.dao.TaskRepository;
import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.model.dto.TaskDTO;
import com.personal.quasar.model.entity.Task;
import com.personal.quasar.model.mapper.TaskMapper;
import com.personal.quasar.service.AuditService;
import com.personal.quasar.service.UserProfileFacade;
import com.personal.quasar.validator.TaskValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {

    public TaskDTO get(String id) {
        return taskRepository.findByIdAndIsDeletedFalse(id)
                .map(taskMapper::entityToDTO)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Autowired
    private UserProfileFacade userProfileFacade;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private AuditService auditService;

    @Autowired
    private TaskValidator taskValidator;

    public TaskDTO create(TaskDTO task) throws InvalidFieldException {
        log.info("create method is started with task - {}", task);
        var newTask = taskMapper.dtoToEntity(task);
        taskValidator.validateScheduledDate(task.getScheduledDate());
        auditService.populateAuditFields(newTask);
        newTask.setId(UUID.randomUUID().toString());
        log.info("creating task - {}", newTask);
        Task savedTask = taskRepository.save(newTask);
        return taskMapper.entityToDTO(savedTask);
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAllByIsDeletedFalse()
                .map(x -> x.stream().map(taskMapper::entityToDTO).toList())
                .orElseThrow(() -> new RuntimeException("No tasks found"));
    }

    public List<TaskDTO> getAllTasksByDate(Date date) {
        log.info("getAllTasksByDate is started with the date - {}", date);
        var currentUser = userProfileFacade.getActiveUserId();
        return taskRepository.findByScheduledDateAndIsDeletedFalseAndCreatedByOrderByCreatedDateAsc(date, currentUser)
                .orElse(new ArrayList<>())
                .stream()
                .map(taskMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    public void delete(String id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setIsDeleted(true);
        auditService.populateAuditFields(task);
        taskRepository.save(task);
    }

    public TaskDTO update(String id, TaskDTO updatedTask) throws InvalidFieldException {
        taskValidator.validateScheduledDate(updatedTask.getScheduledDate());
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (equalsIgnoringAuditFields(updatedTask, existingTask)) {
            return updatedTask;
        }

        existingTask.setName(updatedTask.getName());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.getCompleted());

        auditService.populateAuditFields(existingTask);

        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.entityToDTO(savedTask);
    }

    private boolean equalsIgnoringAuditFields(TaskDTO task1, Task task2) {
        return task1.getName().equals(task2.getName()) &&
               task1.getDescription().equals(task2.getDescription()) &&
               task1.getCompleted().equals(task2.getCompleted());
    }
}