package com.personal.quasar.service;

import com.personal.quasar.dao.TaskRepository;
import com.personal.quasar.model.dto.TaskDTO;
import com.personal.quasar.model.entity.Task;
import com.personal.quasar.model.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    public TaskDTO create(TaskDTO task) {
        var newTask = taskMapper.dtoToEntity(task);
        newTask.setCreatedBy("system"); // Replace with actual user if available
        newTask.setCreatedDate(new Date());
        newTask.setLastModifiedBy("system"); // Replace with actual user if available
        newTask.setLastModifiedDate(new Date());
        newTask.setId(UUID.randomUUID().toString());
        Task savedTask = taskRepository.save(newTask);
        return taskMapper.entityToDTO(savedTask);
    }

    public TaskDTO get(String id) {
        return taskRepository.findByIdAndIsDeletedFalse(id)
                .map(taskMapper::entityToDTO)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAllByIsDeletedFalse()
                .map(x -> x.stream().map(taskMapper::entityToDTO).toList())
                .orElseThrow(() -> new RuntimeException("No tasks found"));
    }

    public void delete(String id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setIsDeleted(true);
        taskRepository.save(task);
    }

    public TaskDTO update(String id, TaskDTO updatedTask) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (equalsIgnoringAuditFields(updatedTask, existingTask)) {
            return updatedTask;
        }

        existingTask.setName(updatedTask.getName());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.getCompleted());
        existingTask.setLastModifiedBy("system");
        existingTask.setLastModifiedDate(new Date());

        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.entityToDTO(savedTask);
    }

    private boolean equalsIgnoringAuditFields(TaskDTO task1, Task task2) {
        return task1.getName().equals(task2.getName()) &&
               task1.getDescription().equals(task2.getDescription()) &&
               task1.getCompleted().equals(task2.getCompleted());
    }
}