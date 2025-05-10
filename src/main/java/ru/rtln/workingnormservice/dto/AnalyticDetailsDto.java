package ru.rtln.workingnormservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Модель данных для представления данных аналитики.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для аналитики")
public class AnalyticDetailsDto {

    private Long userId;

    private String email;

    private Long productId;

    private Integer totalWorkingNorm;

    private Integer totalCurrentNorm;

    private Double totalScore;
}


