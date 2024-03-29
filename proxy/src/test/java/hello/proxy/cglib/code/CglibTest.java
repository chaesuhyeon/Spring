package hello.proxy.cglib.code;

import hello.proxy.common.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    /**
     * 인터페이스가 없는데 동적 프록시 만들 수 있는지 확인
     */
    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class); // 구체 클래스를 기반으로 ConcreteService를 상속받은 프록시를 만들어야 한다.
        enhancer.setCallback(new TimeMethodInterceptor(target));
        ConcreteService proxy = (ConcreteService) enhancer.create();// 프록시 생성

        log.info("targetClass={}", target.getClass()); // targetClass=class hello.proxy.common.service.ConcreteService
        log.info("proxyClass={}", proxy.getClass()); // proxyClass=class hello.proxy.common.service.ConcreteService$$EnhancerByCGLIB$$25d6b0e3

        proxy.call();
    }
}
