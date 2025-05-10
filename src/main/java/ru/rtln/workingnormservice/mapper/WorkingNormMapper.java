package ru.rtln.workingnormservice.mapper;

import org.mapstruct.Mapper;
import ru.rtln.workingnormservice.dto.WorkingNormDto;
import ru.rtln.workingnormservice.entity.WorkingNorm;

@Mapper(componentModel = "spring")
public abstract class WorkingNormMapper implements Mappable<WorkingNorm, WorkingNormDto> {

    @Override
    public WorkingNorm toEntity(WorkingNormDto dto) {
        return WorkingNorm.builder()
                .id(dto.getId())
                .productId(dto.getProductId())
                .userId(dto.getEmployeeId())
                .deadline(dto.getDeadline())
                .workingCount(dto.getWorkingCount())
                .createdAt(dto.getCreatedAt())
                .currentCount(dto.getCurrentCount())
                .status(dto.getStatus())
                .build();
    }

    @Override
    public WorkingNormDto toDto(WorkingNorm entity) {
        return WorkingNormDto.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .employeeId(entity.getUserId())
                .workingCount(entity.getWorkingCount())
                .currentCount(entity.getCurrentCount())
                .currentCount(entity.getCurrentCount())
                .deadline(entity.getDeadline())
                .createdAt(entity.getCreatedAt())
                .status(entity.getStatus())
                .build();
    }
}
