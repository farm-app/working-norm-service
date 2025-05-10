package ru.rtln.workingnormservice.advice;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.rtln.workingnormservice.dto.exception.ErrorMessage;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ValidationControllerAdvice {

    @ExceptionHandler({
            BindException.class,
            ConstraintViolationException.class,
            HandlerMethodValidationException.class,
            MissingServletRequestParameterException.class,
            MaxUploadSizeExceededException.class
    })
    public ResponseEntity<ErrorMessage> handleValidationException(Exception exception) {
        return formErrorMessage(exception);
    }

    private ResponseEntity<ErrorMessage> formErrorMessage(Exception exception) {
        var args = new HashMap<String, Object>();

        if (exception instanceof BindException ex) {
            ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
                args.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
        } else if (exception instanceof ConstraintViolationException ex) {
            ex.getConstraintViolations()
                    .forEach(constraintViolation -> args.put(constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getMessage()));
        } else if (exception instanceof HandlerMethodValidationException ex) {
            var errors = ex.getAllErrors();
            errors.forEach(error -> args.put(
                    Objects.requireNonNull(error.getCodes())[0],
                    error.getDefaultMessage())
            );
        } else if (exception instanceof MissingServletRequestParameterException ex) {
            args.put(ex.getParameterName(), ex.getMessage());
        } else if (exception instanceof MaxUploadSizeExceededException ex) {
            args.put(ex.getBody().getDetail(), ex.getCause().getMessage());
        }

        var validationErrorMessage = "A validation error occurred while contacting the server";
        String validationErrorCode = "VALIDATION_ERROR";
        var errorMessage = ErrorMessage.builder()
                .code(validationErrorCode)
                .userMessage("Произошла ошибка валидации")
                .devMessage(validationErrorMessage)
                .args(args)
                .build();
        var response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);

        log.error("{}: {}: {}", validationErrorCode, validationErrorMessage, args, exception);
        return response;
    }
}
