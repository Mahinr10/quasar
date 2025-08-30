package com.personal.quasar.model.dto;

import com.personal.quasar.model.enums.TaskPriority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskDTO extends BaseDTO {
    private String name;
    private String description;
    private Boolean completed;
    private Date ScheduledDate = new Date();
    private TaskPriority priority = TaskPriority.MEDIUM;
}
