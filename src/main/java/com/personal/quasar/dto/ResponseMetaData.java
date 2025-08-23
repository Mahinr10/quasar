package com.personal.quasar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMetaData {
    private Integer status;
    private String message;
    private Boolean error;
}