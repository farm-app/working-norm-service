
package ru.rtln.workingnormservice.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ErrorMessage {

    private String code;

    private String userMessage;

    private String devMessage;

    private Map<String, Object> args;
}
