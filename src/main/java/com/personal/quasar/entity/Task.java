package com.personal.quasar.entity;

import com.personal.quasar.enums.TaskPriority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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