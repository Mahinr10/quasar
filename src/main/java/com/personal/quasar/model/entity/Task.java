package com.personal.quasar.model.entity;

import com.personal.quasar.model.enums.TaskPriority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("task")
public class Task extends AuditEntity {
    private String name;
    private String description;
    private Boolean completed = false;
    private Date ScheduledDate = new Date();
    private TaskPriority priority = TaskPriority.MEDIUM;
}