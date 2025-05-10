package ru.rtln.workingnormservice.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.rtln.workingnormservice.dto.filter.ReportFilter;
import ru.rtln.workingnormservice.repository.FilterWorkingNormRepository;
import ru.rtln.workingnormservice.util.QPredicates;

import java.util.List;

import static ru.rtln.workingnormservice.entity.QWorkingNorm.workingNorm;

@RequiredArgsConstructor
@Repository
public class FilterWorkingNormRepositoryImpl implements
        FilterWorkingNormRepository {

    private final EntityManager entityManager;

    @Override
    public List<Tuple> findAnalyticsByProductDetailFilter(ReportFilter filter) {
        Predicate predicate = QPredicates.builder()
                .add(filter.getEmployeeId(), workingNorm.userId::eq)
                .add(filter.getProductId(), workingNorm.productId::eq)
                .add(filter.getStartTime(), workingNorm.deadline::goe)
                .add(filter.getEndTime(), workingNorm.deadline::loe)
                .buildAnd();
        return new JPAQuery<Tuple>(entityManager)
                .select(workingNorm.userId,
                        workingNorm.email,
                        workingNorm.productId,
                        workingNorm.workingCount.sum(),
                        workingNorm.currentCount.sum())
                .from(workingNorm)
                .groupBy(workingNorm.userId, workingNorm.email, workingNorm.productId)
                .where(predicate)
                .fetch();
    }

    @Override
    public List<Tuple> findReportByProductDetailFilter(ReportFilter reportFilter) {
        Predicate predicate = QPredicates.builder()
                .add(reportFilter.getEmployeeId(), workingNorm.userId::eq)
                .add(reportFilter.getProductId(), workingNorm.productId::eq)
                .add(reportFilter.getStartTime(), workingNorm.updatedAt::goe)
                .add(reportFilter.getEndTime(), workingNorm.updatedAt::loe)
                .buildAnd();
        return new JPAQuery<Tuple>(entityManager)
                .select(workingNorm.productId, workingNorm.currentCount.sum())
                .from(workingNorm)
                .groupBy(workingNorm.productId)
                .where(predicate)
                .orderBy(workingNorm.currentCount.sum().desc())
                .fetch();
    }
}
