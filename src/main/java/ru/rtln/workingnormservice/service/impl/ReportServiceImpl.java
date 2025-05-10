package ru.rtln.workingnormservice.service.impl;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rtln.workingnormservice.dto.AnalyticDetailsDto;
import ru.rtln.workingnormservice.dto.StatisticReportDto;
import ru.rtln.workingnormservice.dto.filter.ReportFilter;
import ru.rtln.workingnormservice.repository.FilterWorkingNormRepository;
import ru.rtln.workingnormservice.service.ReportService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final FilterWorkingNormRepository workingNormRepository;

    @Override
    public List<AnalyticDetailsDto> getAnalyticsReportByFilter(ReportFilter filter) {
        List<Tuple> analytics = workingNormRepository.findAnalyticsByProductDetailFilter(filter);
        return getAnalyticDetailsDtos(analytics);
    }

    @Override
    public Map<Long, Double> calculatingEfficiencyByEmployees(List<AnalyticDetailsDto> analytics) {
        return Map.of();
    }

    @Override
    public List<StatisticReportDto> getStatisticReportByFilter(ReportFilter filter) {
        return workingNormRepository.findReportByProductDetailFilter(filter).stream()
                .map(this::getProductDetailsDto)
                .toList();
    }

    private List<AnalyticDetailsDto> getAnalyticDetailsDtos(List<Tuple> analytics) {
        return analytics.stream().map(tuple -> {
            Integer totalWorkingNorm = tuple.get(3, Integer.class);
            Integer totalCurrentNorm = tuple.get(4, Integer.class);
            Double totalScore = getTotalScoreRounded(totalCurrentNorm, totalWorkingNorm);
            return getAnalyticDetailsDto(tuple, totalWorkingNorm, totalCurrentNorm, totalScore);
        }).toList();
    }

    private AnalyticDetailsDto getAnalyticDetailsDto(Tuple tuple,
                                                     Integer totalWorkingNorm,
                                                     Integer totalCurrentNorm,
                                                     Double totalScore) {
        return AnalyticDetailsDto.builder()
                .userId(tuple.get(0, Long.class))
                .email(tuple.get(1, String.class))
                .productId(tuple.get(2, Long.class))
                .totalWorkingNorm(totalWorkingNorm)
                .totalCurrentNorm(totalCurrentNorm)
                .totalScore(totalScore)
                .build();
    }

    private StatisticReportDto getProductDetailsDto(Tuple tuple) {
        Long productId = tuple.get(0, Long.class);
        Integer count = tuple.get(1, Integer.class);
        return StatisticReportDto.builder()
                .productId(productId)
                .count(count)
                .build();
    }

    private Double getTotalScoreRounded(Integer totalCurrentNorm, Integer totalWorkingNorm) {
        double result = ((double) totalCurrentNorm / totalWorkingNorm) * 100;
        return Math.round(result * 100.0) / 100.0;
    }
}
