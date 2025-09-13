package com.personal.quasar.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Document(collection = "timezones")
public class TimeZone extends AuditEntity {
    private String timeZoneId;
}
