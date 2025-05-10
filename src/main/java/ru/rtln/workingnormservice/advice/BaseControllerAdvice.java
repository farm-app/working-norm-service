package ru.rtln.workingnormservice.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.rtln.workingnormservice.dto.exception.ErrorMessage;
import ru.rtln.workingnormservice.exception.BaseException;
import ru.rtln.workingnormservice.util.MessageHelper;

@Slf4j
@RequiredArgsConstructor
public class BaseControllerAdvice {

    private final MessageHelper messageHelper;

    protected ResponseEntity<ErrorMessage> formErrorMsg(BaseException ex,
                                                        HttpStatus status,
                                                        String code,
                                                        String userMessageProperty) {
        var userMessage = messageHelper.getMessage(userMessageProperty);
        if (userMessage != null && ex.getMessageParam() != null) {
            userMessage = userMessage.formatted(ex.getMessageParam());
        }
        var args = ex.getArgs();
        var devMessage = ex.getMessage();
        log.error("{} : {}", code, ex.getArgsAsString(), ex);
        return ResponseEntity.status(status)
                .body(ErrorMessage.builder()
                        .userMessage(userMessage)
                        .devMessage(devMessage)
                        .args(args)
                        .code(code)
                        .build());
    }
}
