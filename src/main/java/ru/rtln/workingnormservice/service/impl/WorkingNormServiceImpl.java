package ru.rtln.workingnormservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rtln.workingnormservice.aop.annotation.Loggable;
import ru.rtln.workingnormservice.client.ProductApiClient;
import ru.rtln.workingnormservice.client.UserApiClient;
import ru.rtln.workingnormservice.dto.ManufacturedProductDto;
import ru.rtln.workingnormservice.dto.ProductResponse;
import ru.rtln.workingnormservice.dto.UserResponse;
import ru.rtln.workingnormservice.dto.WorkingNormDto;
import ru.rtln.workingnormservice.dto.filter.WorkingNormFilter;
import ru.rtln.workingnormservice.entity.WorkingNorm;
import ru.rtln.workingnormservice.enums.Status;
import ru.rtln.workingnormservice.exception.WorkingNormException;
import ru.rtln.workingnormservice.mapper.WorkingNormMapper;
import ru.rtln.workingnormservice.repository.WorkingNormRepository;
import ru.rtln.workingnormservice.service.WorkingNormService;
import ru.rtln.workingnormservice.util.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static ru.rtln.workingnormservice.exception.WorkingNormException.Code.INVALID_DEADLINE;
import static ru.rtln.workingnormservice.exception.WorkingNormException.Code.WORKING_NORM_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class WorkingNormServiceImpl implements WorkingNormService {

    private final WorkingNormRepository workingNormRepository;
    private final WorkingNormMapper workingNormMapper;
    private final ProductApiClient productApiClient;
    private final UserApiClient userApiClient;

    @Override
    @Transactional
    @Loggable
    public WorkingNormDto create(WorkingNormDto workingNormDto) {
        checkDeadlineValid(workingNormDto);
        Optional<WorkingNorm> mayBeWorkingNorm = getInProgressWorkingNormByProductIdAndEmployeeId(workingNormDto.getProductId(), workingNormDto.getEmployeeId());
        if (mayBeWorkingNorm.isPresent()) {
            throw WORKING_NORM_ALREADY_EXISTS.getWith("Рабочая норма уже существует").setMessageParam(mayBeWorkingNorm.get().getId().toString());
        }
        ProductResponse product = apiCall(() -> productApiClient.getProductById(workingNormDto.getProductId()));
        UserResponse user = apiCall(() -> userApiClient.getUserById(workingNormDto.getProductId()));
        checkValidUser(user);
        WorkingNorm workingNorm = createWorkingNorm(workingNormDto, product, user);
        workingNormRepository.save(workingNorm);
        return workingNormMapper.toDto(workingNorm);
    }

    @Override
    @Transactional
    @Loggable
    public WorkingNormDto produceProduct(ManufacturedProductDto manufacturedProductDto, Long userId) {
        WorkingNormDto accessWorkingNorm = getAccessWorkingNorm(manufacturedProductDto, userId);
        return updateWorkingNorm(accessWorkingNorm, manufacturedProductDto.getCount());
    }

    private static void checkDeadlineValid(WorkingNormDto workingNormDto) {
        if (workingNormDto.getDeadline().isBefore(LocalDateTime.now())) {
            throw INVALID_DEADLINE.getWith("Нельзя создать рабочую норму задним числом");
        }
        LocalDateTime offsetTime = workingNormDto.getDeadline().plusHours(1);
        if (!LocalDateTimeUtil.isTodayDeadlineBefore(offsetTime, 22, 0)) {
            throw INVALID_DEADLINE.getWith("Рабочий день заканчивается в 18:00. Последнюю задачу можно выдать в 17:00.");
        }
    }

    @Loggable
    private WorkingNormDto getAccessWorkingNorm(ManufacturedProductDto manufacturedProductDto, Long employeeId) {
        Optional<WorkingNorm> mayBeWorkingNorm = getInProgressWorkingNormByProductIdAndEmployeeId(manufacturedProductDto.getProductId(), employeeId);
        if (mayBeWorkingNorm.isEmpty()) {
            throw WorkingNormException.Code.WORKING_NORM_NOT_FOUND.getWith("Рабочей нормы нет");
        }
        return workingNormMapper.toDto(mayBeWorkingNorm.get());
    }

    @Loggable
    private WorkingNormDto updateWorkingNorm(WorkingNormDto workingNormDto, Integer count) {
        Optional<WorkingNorm> mayBeWorkingNorm = workingNormRepository.findById(workingNormDto.getId());
        if (mayBeWorkingNorm.isEmpty()) {
            throw WorkingNormException.Code.WORKING_NORM_NOT_FOUND.getWith("Рабочей нормы нет");
        }
        WorkingNorm workingNorm = mayBeWorkingNorm.get();
        updateCurrentCount(count, workingNorm);
        workingNormRepository.save(workingNorm);
        return workingNormMapper.toDto(workingNorm);
    }

    private void updateCurrentCount(Integer count, WorkingNorm workingNorm) {
        int updatedCurrentNorm = workingNorm.getCurrentCount() + count;
        var workingCount = workingNorm.getWorkingCount();
        if (updatedCurrentNorm >= workingCount) {
            workingNorm.setStatus(Status.DONE);
        }
        workingNorm.setCurrentCount(updatedCurrentNorm);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<WorkingNormDto> getWorkingNormsByFilter(Long userId, WorkingNormFilter workingNormFilter) {
        List<WorkingNorm> workingNormsByFilter =
                workingNormRepository.findByUserIdAndStatusAndDeadlineBetweenOrderByDeadline(
                        userId,
                        workingNormFilter.getStatus(),
                        workingNormFilter.startTime,
                        workingNormFilter.endTime
                );
        return workingNormMapper.toDto(workingNormsByFilter);
    }

    @Override
    @Transactional
    @Loggable
    public void updateWorkingNorm(WorkingNormDto workingNormDto) {
        WorkingNorm workingNorm = workingNormMapper.toEntity(workingNormDto);
        workingNormRepository.save(workingNorm);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<WorkingNormDto> getWorkingNormByStatus(Status status) {
        List<WorkingNorm> workingNormsByStatus = workingNormRepository.findByStatus(status);
        return workingNormMapper.toDto(workingNormsByStatus);
    }

    private Optional<WorkingNorm> getInProgressWorkingNormByProductIdAndEmployeeId(Long productId, Long userId) {
        return workingNormRepository.findByProductIdAndUserIdAndStatus(productId, userId, Status.IN_PROGRESS);
    }


    private void checkValidUser(UserResponse user) {
        if (!user.isActive()) {
            throw WorkingNormException.Code.USER_NO_ACTIVE.getWith(String.format("Аккаунт пользователя с [%s] заблокирован", user.getId())).setMessageParam(user.getUsername());
        }
    }

    private WorkingNorm createWorkingNorm(WorkingNormDto workingNormDto, ProductResponse product, UserResponse user) {
        return WorkingNorm.builder()
                .productId(product.getId())
                .userId(user.getId())
                .email(user.getEmail())
                .workingCount(workingNormDto.getWorkingCount())
                .currentCount(0)
                .deadline(workingNormDto.getDeadline())
                .status(Status.IN_PROGRESS)
                .build();
    }

    private <T> T apiCall(Supplier<ResponseEntity<T>> apiCall) {
        final var responseBody = apiCall.get().getBody();
        assert responseBody != null;
        return responseBody;
    }

}
