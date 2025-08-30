package com.personal.quasar.model.dto;

import com.personal.quasar.model.enums.TaskPriority;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskDTO extends BaseDTO {
    @NotNull
    @Size(min = 3, max = 100)
    private String name;
    @Size(max = 1024)
    private String description;
    private Boolean completed;
    private Date ScheduledDate = new Date();
    private TaskPriority priority = TaskPriority.MEDIUM;
}
