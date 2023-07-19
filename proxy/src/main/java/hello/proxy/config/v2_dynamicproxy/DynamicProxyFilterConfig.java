package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceFilterHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

/**
 * JDK 동적 프록시 기술을 사용해서 Controller, Service, Repository에 맞는 동적 프록시를 생성
 * LogTraceBasicHandler : 동적 프록시를 만들더라도 LogTrace를 출력하는 로직은 모두 같기 때문에 프록시는 모두 LogTraceBasicHandler를 사용
 */
@Configuration
public class DynamicProxyFilterConfig {

    // 메서드 이름 필터 패턴
    private static final String[] PATTERNS = {"request*", "orders*", "save*"}; // request, orders, save로 시작하는 메서드는 로그를 출력한다.

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderServiceV1(logTrace)); // orderServiceV1 반환 값이 proxy이므로 proxy를 주입

        // proxy 생성
        OrderControllerV1 proxy = (OrderControllerV1) Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class},
                new LogTraceFilterHandler(orderController, logTrace, PATTERNS));

        return proxy;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace){
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepositoryV1(logTrace)); // orderRepositoryV1의 반환 값이 proxy이므로 proxy를 주입

        // proxy 생성
        OrderServiceV1 proxy = (OrderServiceV1) Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class},
                new LogTraceFilterHandler(orderService, logTrace, PATTERNS));

        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();

        // proxy 생성
        OrderRepositoryV1 proxy = (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                new LogTraceFilterHandler(orderRepository, logTrace, PATTERNS));

        return proxy;
    }
}
