package ru.rtln.workingnormservice.service;

import ru.rtln.workingnormservice.dto.AnalyticDetailsDto;
import ru.rtln.workingnormservice.dto.StatisticReportDto;
import ru.rtln.workingnormservice.dto.filter.ReportFilter;

import java.util.List;
import java.util.Map;

public interface ReportService {

    List<AnalyticDetailsDto> getAnalyticsReportByFilter(ReportFilter reportFilter);


    /**
     * Возвращает коэффициент выработки рабочей нормы по всем сотрудникам на основании аналитического отчета.
     * <p>
     * Производится вычисление КПД рабочей нормы в соответствии с данными аналитического отчета.
     * Это становится возможным благодаря группировке данных по идентификатору сотрудника и использованию
     * в качестве агрегирующего значения среднее значение выработки рабочей нормы. То есть отношение
     * фактически суммарного собранного количества продуктов к суммарному количеству, которое необходимо было
     * собрать согласно норме.
     *
     * @param analytics аналитический отчет
     * @return коэффициент выработки рабочей нормы по сотрудникам
     */
    Map<Long, Double> calculatingEfficiencyByEmployees(List<AnalyticDetailsDto> analytics);


    List<StatisticReportDto> getStatisticReportByFilter(ReportFilter reportFilter);

}
