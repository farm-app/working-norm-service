package ru.rtln.workingnormservice.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
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
 * Модель данных для получения рабочих норм для сотрудника по фильтру.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель данных для получения рабочих норм по фильтру")
public class WorkingNormFilter {

    /**
     * Начало периода для получения рабочих норм
     */
    @NotNull(message = "Начало периода не может быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime startTime;

    /**
     * Конец периода для получения рабочих норм
     */
    @NotNull(message = "Конец периода не может быть пустым")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime endTime;

    /**
     * Статус рабочей нормы
     */
    @NotNull(message = "Статус не может быть пустым")
    public Status status;
}
