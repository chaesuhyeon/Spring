package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 순서
 * client가 controller proxy를 호출하게 만들고 proxy가 controller 구현체를 호출하게 만든다.
 * controller 구현체가 service proxy를 호출하고 proxy가 service 구현체를 호출한다.
 * 프록시를 실제 스프링 빈 대신에 등록한다.
 */
@Configuration
public class InterfaceProxyConfig {
    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace){
        OrderControllerV1Impl controllerV1Impl = new OrderControllerV1Impl(orderService(logTrace)); // 구현체는 service proxy를 호출해야한다.
        return new OrderControllerInterfaceProxy(controllerV1Impl, logTrace); // 프록시를 스프링 빈으로 등록
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }
}
