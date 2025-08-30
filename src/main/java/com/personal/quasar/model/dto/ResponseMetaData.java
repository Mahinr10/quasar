package com.personal.quasar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMetaData {
    private String id;
    private Integer status;
    private String message;
    private Boolean error;
}