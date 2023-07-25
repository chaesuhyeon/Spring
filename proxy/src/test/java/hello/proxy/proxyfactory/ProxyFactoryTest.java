package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ProxyFactoryTest {
    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target); // proxyFactory를 생성할 때 target 정보를 넣어준다.
        proxyFactory.addAdvice(new TimeAdvice());

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass()); // targetClass = class hello.proxy.common.service.ServiceImpl
        log.info("proxyClass = {}", proxy.getClass()); // proxyClass = class jdk.proxy3.$Proxy13

        proxy.save();

        // AopUtils는 proxyFactory를 통해서 생성한 프록시에만 사용 가능
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();

/*        12:39:21.090 [Test worker] INFO hello.proxy.common.advice.TimeAdvice - TimeProxy 실행
        12:39:21.091 [Test worker] INFO hello.proxy.common.service.ServiceImpl - save 호출
        12:39:21.091 [Test worker] INFO hello.proxy.common.advice.TimeAdvice - TimeProxy 종료 resultTime = 0*/
    }
}
