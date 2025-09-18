package com.personal.quasar.common.exception;

import com.personal.quasar.model.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static com.personal.quasar.util.ExceptionConstants.UNPRIVILEGED_TO_MODIFICATION_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceDoesNotExistException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceDoesNotExistException(ResourceDoesNotExistException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponseDTO.setError("Invalid Id");
        errorResponseDTO.setMessage(ex.getMessage());
        errorResponseDTO.setPath(request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    @ExceptionHandler(ImmutableFieldModificationException.class)
    public ResponseEntity<ErrorResponseDTO> handleImmutableFieldModificationException(ImmutableFieldModificationException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDTO.setError("Immutable Field Modification");
        errorResponseDTO.setMessage(ex.getMessage());
        errorResponseDTO.setPath(request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }


    @ExceptionHandler(UnprivilegedToModificationException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnprivilegedToModificationException(UnprivilegedToModificationException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDTO.setError(UNPRIVILEGED_TO_MODIFICATION_ERROR);
        errorResponseDTO.setMessage(ex.getMessage());
        errorResponseDTO.setPath(request.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDTO);
    }
}
