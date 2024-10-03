package com.summerelvn.saga.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String title;

    private String message;

    private Map<String, String> details;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;

    public ErrorResponse(String title, String message, Map details) {
        this.title = title;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String title, String message) {
        this(title, message, null);
    }
    public ErrorResponse(String title) {
        this(title, null);
    }

}
