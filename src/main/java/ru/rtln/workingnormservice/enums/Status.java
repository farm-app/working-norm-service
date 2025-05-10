package ru.rtln.workingnormservice.enums;

/**
 * Перечисление статусов рабочей нормы.
 *
 */
public enum Status {

    /**
     * Рабочая норма в процессе выполнения
     */
    IN_PROGRESS,

    /**
     * Рабочая норма выполнена
     */
    DONE,

    /**
     * Рабочая норма просрочена
     */
    OVERDUE
}
