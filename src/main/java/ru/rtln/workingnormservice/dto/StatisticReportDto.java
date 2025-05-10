package ru.rtln.workingnormservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Модель данных для представления отчета.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для отчета")
public class StatisticReportDto {

    /**
     * Название продукта
     */
    private Long productId;

    /**
     * Количество произведенного продукта
     */
    private Integer count;
}
