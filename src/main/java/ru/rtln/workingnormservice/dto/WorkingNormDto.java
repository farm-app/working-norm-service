package ru.rtln.workingnormservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.rtln.workingnormservice.enums.Status;

import java.time.LocalDateTime;

/**
 * Модель данных для рабочей нормы.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для рабочей нормы")
public class WorkingNormDto {

    /**
     * Идентификатор
     */
    private Long id;

    /**
     * Идентификатор продукта, для которого будет сформирована рабочая норма
     */
    @NotNull(message = "Идентификатор продукта не может отсутствовать")
    private Long productId;

    /**
     * Идентификатор сотрудника, для которого будет сформирована рабочая норма
     */
    @NotNull(message = "Идентификатор сотрудника не может отсутствовать")
    private Long employeeId;

    /**
     * Необходимое рабочее количество по производству
     */
    @NotNull(message = "Размер рабочей нормы не может быть пустым")
    @Min(value = 1, message = "Размер рабочей нормы не может быть меньше 1")
    private Integer workingCount;

    /**
     * Текущее количество собранного продукта
     */
    private Integer currentCount;

    /**
     * Крайний срок выполнения рабочей нормы
     */
    @NotNull(message = "Срок выполнения рабочей нормы не может быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;

    private LocalDateTime createdAt;

    /**
     * Статус рабочей нормы
     */
    private Status status;
}
