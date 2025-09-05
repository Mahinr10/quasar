package com.personal.quasar.service;

import com.personal.quasar.UnitTest;
import com.personal.quasar.dao.TaskRepository;
import com.personal.quasar.common.exception.InvalidFieldException;
import com.personal.quasar.model.dto.TaskDTO;
import com.personal.quasar.model.entity.Task;
import com.personal.quasar.model.mapper.TaskMapper;
import com.personal.quasar.service.impl.TaskService;
import com.personal.quasar.validator.TaskValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class TaskServiceTest extends UnitTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskValidator taskValidator;

    @Mock
    AuditService auditService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void executeBeforeEach() {
        doNothing().when(auditService).populateAuditFields(any());
    }

    @Test
    public void createTest() {
        var taskDTO = getTaskDTO();
        var task = getTask();
        when(taskRepository.findByIdAndIsDeletedFalse(task.getId())).thenReturn(Optional.of(task));
        when(taskMapper.entityToDTO(task)).thenReturn(taskDTO);
        var result = taskService.get(task.getId());
        Assertions.assertEquals(task.getId(), result.getId());
    }

    @Test
    public void createWithInvalidScheduledDate() throws Exception {
        var taskDTO = getTaskDTO();
        var date = new Date();
        taskDTO.setScheduledDate(date);
        doThrow(new InvalidFieldException("Scheduled date must be in the future"))
                .when(taskValidator)
                .validateScheduledDate(date);
        var result = Assertions.assertThrows(InvalidFieldException.class, () -> {
            taskService.create(taskDTO);
        });
        Assertions.assertEquals("Scheduled date must be in the future", result.getMessage());
    }

    @Test
    public void updateWithInvalidScheduledDate() throws Exception {
        var taskDTO = getTaskDTO();
        var date = new Date();
        taskDTO.setScheduledDate(date);
        doThrow(new InvalidFieldException("Scheduled date must be in the future"))
                .when(taskValidator)
                .validateScheduledDate(date);
        var result = Assertions.assertThrows(InvalidFieldException.class, () -> {
            taskService.update(taskDTO.getId(), taskDTO);
        });
        Assertions.assertEquals("Scheduled date must be in the future", result.getMessage());
    }

    private Task getTask() {
        var task = new Task();
        String id = "xyz";
        task.setId(id);
        return task;
    }

    private TaskDTO getTaskDTO() {
        var taskDTO = new TaskDTO();
        String id = "xyz";
        taskDTO.setId(id);
        return taskDTO;
    }
}
