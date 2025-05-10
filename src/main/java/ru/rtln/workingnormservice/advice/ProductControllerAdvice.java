package ru.rtln.workingnormservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rtln.workingnormservice.dto.exception.ErrorMessage;
import ru.rtln.workingnormservice.exception.WorkingNormException;
import ru.rtln.workingnormservice.util.MessageHelper;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ProductControllerAdvice extends BaseControllerAdvice {

    public ProductControllerAdvice(MessageHelper messageHelper) {
        super(messageHelper);
    }

    @ExceptionHandler(WorkingNormException.class)
    public ResponseEntity<ErrorMessage> handleException(WorkingNormException ex) {
        var exceptionCode = ex.getCode();
        var httpStatus = switch (exceptionCode) {
            case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            case WORKING_NORM_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_DEADLINE,
                 WORKING_NORM_ALREADY_EXISTS,
                 USER_NO_ACTIVE -> BAD_REQUEST;
        };
        var userMessageProperty = exceptionCode.getUserMessageProperty();
        return formErrorMsg(ex, httpStatus, exceptionCode.toString(), userMessageProperty);
    }
}
