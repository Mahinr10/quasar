package com.personal.quasar.service;

import com.personal.quasar.dao.TaskRepository;
import com.personal.quasar.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public String create(Task task) {
        // Fill audit fields
        task.setCreatedBy("system"); // Replace with actual user if available
        task.setCreatedDate(new Date());
        task.setLastModifiedBy("system"); // Replace with actual user if available
        task.setLastModifiedDate(new Date());
        task.setId(UUID.randomUUID().toString());
        return taskRepository.save(task).getId();
    }

    public Task get(String id) {
        return taskRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void delete(String id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setIsDeleted(true);
        update(id, task);
    }

    public String update(String id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (equalsIgnoringAuditFields(updatedTask, existingTask)) {
            return existingTask.getId();
        }

        existingTask.setName(updatedTask.getName());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.getCompleted());
        existingTask.setLastModifiedBy("system");
        existingTask.setLastModifiedDate(new Date());

        return taskRepository.save(existingTask).getId();
    }

    private boolean equalsIgnoringAuditFields(Task task1, Task task2) {
        return task1.getName().equals(task2.getName()) &&
               task1.getDescription().equals(task2.getDescription()) &&
               task1.getCompleted().equals(task2.getCompleted());
    }
}