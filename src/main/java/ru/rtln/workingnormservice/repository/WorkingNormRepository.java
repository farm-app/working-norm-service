package ru.rtln.workingnormservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rtln.workingnormservice.entity.WorkingNorm;
import ru.rtln.workingnormservice.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс определяет методы взаимодействия с данными, определенными сущностью {@link WorkingNorm}.
 */
@Repository
public interface WorkingNormRepository extends JpaRepository<WorkingNorm, Long>,
        FilterWorkingNormRepository {


    /**
     * Возвращает рабочую норму по продукту, сотруднику и статусу.
     * Используется для проверки существования рабочей нормы.
     *
     * @param product продукт
     * @param user    сотрудник
     * @param status  статус
     * @return {@code Optional}, содержащий рабочую норму или {@code Optional.empty()},
     * если рабочей нормы не найдено.
     */
    Optional<WorkingNorm> findByProductIdAndUserIdAndStatus(Long productId,
                                                        Long userId,
                                                        Status status);

    List<WorkingNorm> findByUserIdAndStatusAndDeadlineBetweenOrderByDeadline(
            Long userId,
            Status status,
            LocalDateTime startTime,
            LocalDateTime endTime);

    /**
     * Поиск рабочих норм по статусу.
     * <p>
     * Используется для получения списка рабочих норм с заданным статусом,
     * который будет использован для анализа текущего собранного количества по отношению к заданной норме
     * и установленного времени сбора.
     *
     * @param status cтатус
     * @return список рабочих норм
     */
    List<WorkingNorm> findByStatus(Status status);
}
