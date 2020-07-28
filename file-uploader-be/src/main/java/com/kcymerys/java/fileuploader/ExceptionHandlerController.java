package com.kcymerys.java.fileuploader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String,Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.badRequest()
                .body(buildResponse(HttpStatus.BAD_REQUEST,
                        exc.getCause().getMessage().split(":")[1].trim()));
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<Map<String,Object>> handleEntityAlreadyExistException(EntityAlreadyExistException exc) {
        return ResponseEntity.badRequest()
                .body(buildResponse(HttpStatus.BAD_REQUEST, exc.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleEntityNotFoundException(EntityNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildResponse(HttpStatus.NOT_FOUND, exc.getMessage()));
    }

    private Map<String, Object> buildResponse (HttpStatus httpStatus, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", httpStatus.value());
        response.put("message", message);
        return response;
    }

}
