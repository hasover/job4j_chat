package ru.job4j.chat.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@ControllerAdvice
public class EmptyDataExceptionHandler {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(EmptyDataExceptionHandler.class.getSimpleName());

    private final ObjectMapper objectMapper;

    public EmptyDataExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public void handleException(Exception e, HttpServletRequest request,
                                HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(new HashMap<>() { {
                put("message", "Some of fields empty");
                put("details", e.getMessage());
            }}));
        } catch (IOException exception) {
            LOGGER.error(exception.getLocalizedMessage());
        }
        LOGGER.error(e.getMessage());
    }
}
