package com.personal.quasar.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TimeZoneDTO extends AuditDTO {
    private String timeZoneId;
    private Boolean isSelected;
}
