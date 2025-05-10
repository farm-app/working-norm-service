package ru.rtln.workingnormservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rtln.workingnormservice.aop.annotation.Loggable;
import ru.rtln.workingnormservice.dto.WorkingNormDto;
import ru.rtln.workingnormservice.enums.Status;
import ru.rtln.workingnormservice.service.WorkingNormService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingNormUpdateScheduler {

    private final WorkingNormService workingNormService;

    @Scheduled(cron = "${spring.scheduled.cron.expression.every_minute}")
    @Loggable
    public void updateWorkingNormPeriodically() {
        List<WorkingNormDto> workingNorms =
                workingNormService.getWorkingNormByStatus(Status.IN_PROGRESS);
        workingNorms.forEach(
                workingNorm -> {
                    boolean updateStatus = isUpdateStatusWorkingNormByConditions(workingNorm);
                    if (updateStatus) {
                        workingNormService.updateWorkingNorm(workingNorm);
                    }
                }
        );
    }

    private boolean isUpdateStatusWorkingNormByConditions(WorkingNormDto workingNormDto) {
        if (workingNormDto.getDeadline().isBefore(LocalDateTime.now())) {
            workingNormDto.setStatus(Status.OVERDUE);
            return true;
        }
        return false;
    }

}
