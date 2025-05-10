package ru.rtln.workingnormservice.service;

import ru.rtln.workingnormservice.dto.ManufacturedProductDto;
import ru.rtln.workingnormservice.dto.WorkingNormDto;
import ru.rtln.workingnormservice.dto.filter.WorkingNormFilter;
import ru.rtln.workingnormservice.enums.Status;

import java.util.List;

public interface WorkingNormService {

    WorkingNormDto create(WorkingNormDto workingNormDto);

    WorkingNormDto produceProduct(ManufacturedProductDto productDto, Long userId);

    /**
     * Осуществляет получение списка рабочих для конкретного сотрудника по фильтру.
     *
     * @param employeeId        идентификатор сотрудника, для которого будут получены рабочие нормы
     * @param workingNormFilter фильтр для получения рабочих норм
     * @return список рабочих норм
     */
    List<WorkingNormDto> getWorkingNormsByFilter(Long employeeId, WorkingNormFilter workingNormFilter);

    void updateWorkingNorm(WorkingNormDto workingNorm);

    List<WorkingNormDto> getWorkingNormByStatus(Status status);
}
