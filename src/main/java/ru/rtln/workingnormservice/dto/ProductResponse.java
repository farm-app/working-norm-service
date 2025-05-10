package ru.rtln.workingnormservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * Модель данных для продукта.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Модель данных для продукта")
public class ProductResponse {

    /**
     * Идентификатор продукта
     */
    private Long id;

    /**
     * Название продукта
     */
    @NotNull(message = "Название не может отсутствовать")
    @Length(min = 2, max = 64, message = "Название должно содержать от 2 до 64 символов")
    private String name;

    /**
     * Название единицы измерения
     */
    @NotNull(message = "Единица измерения не может отсутствовать")
    @Length(min = 1, max = 16, message = "Единица измерения должна содержать от 1 до 16 символов")
    private String unit;

    private String productPictureUrl;
}
