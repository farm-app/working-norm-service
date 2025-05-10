package ru.rtln.workingnormservice.aop.pointcut;


import ru.rtln.workingnormservice.aop.annotation.Loggable;

/**
 * Класс для определения точек среза в программе.
 */
public class Pointcut {

    /**
     * Определяет все классы, которые находятся в пакете ru.relex.backend.service.impl.
     */
    @org.aspectj.lang.annotation.Pointcut("within(ru.rtln.workingnormservice.service.impl.*)")
    public void isServiceLayer() {
    }

    /**
     * Определяет все такие методы, которые находятся в пакете ru.relex.backend.service.impl и
     * имеют аннотацию {@link Loggable}. Вызовы этих методов должны быть залогированы.
     * <p>
     * Добавление условия {@link  Pointcut#isServiceLayer()} сделано для повышения производительности,
     * чтобы избежать полного сканирования всех методов в программе, так как
     * логирование на начальном этапе разработки рассчитано только на сервисный слой.
     */
    @org.aspectj.lang.annotation.Pointcut("isServiceLayer() && @annotation(ru.rtln.workingnormservice.aop.annotation.Loggable)")
    public void callLoggableServiceLayerMethod() {
    }
}
