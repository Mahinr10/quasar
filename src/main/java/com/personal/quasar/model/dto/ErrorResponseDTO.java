package com.personal.quasar.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponseDTO {
    private int status;
    private String error;
    private String message;
    private String path;
}
