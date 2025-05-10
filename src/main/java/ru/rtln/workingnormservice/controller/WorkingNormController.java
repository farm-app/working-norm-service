package ru.rtln.workingnormservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.rtln.common.security.model.AuthenticatedUserModel;
import ru.rtln.workingnormservice.annotation.HasWorkingNormManagementAuthority;
import ru.rtln.workingnormservice.dto.ManufacturedProductDto;
import ru.rtln.workingnormservice.dto.WorkingNormDto;
import ru.rtln.workingnormservice.dto.filter.WorkingNormFilter;
import ru.rtln.workingnormservice.service.WorkingNormService;

import java.util.List;

@RestController
@RequestMapping("/working-norms")
@RequiredArgsConstructor
@Tag(name = "Working Norms", description = "Working Norms API")
public class WorkingNormController {

    private final WorkingNormService workingNormService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавление рабочей нормы")
    @HasWorkingNormManagementAuthority
    public WorkingNormDto addWorkingNorm(@Validated @RequestBody WorkingNormDto workingNormDto) {
        return workingNormService.create(workingNormDto);
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Производство продукта")
    public WorkingNormDto produceProduct(@AuthenticationPrincipal AuthenticatedUserModel authUser,
                                         @Validated @RequestBody ManufacturedProductDto productDto) {
        return workingNormService.produceProduct(productDto, authUser.getId());
    }

    @GetMapping
    @Operation(summary = "Получение рабочих норм")
    public List<WorkingNormDto> getWorkingNorms(@AuthenticationPrincipal AuthenticatedUserModel authUser,
                                                @Validated WorkingNormFilter workingNormFilter) {
        return workingNormService.getWorkingNormsByFilter(authUser.getId(), workingNormFilter);
    }

}