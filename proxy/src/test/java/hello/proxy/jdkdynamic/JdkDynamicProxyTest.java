package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

/**
 * new TimeInvocationHandler(target) : 동적 프록시에 적용할 핸들러 로직
 * Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler)
 *    - 동적 프록시는 java.lang.reflect.Proxy를 통해서 생성할 수 있음
 *    - 클래스 로더 정보, 인터페이스 그리고 핸들러 로직을 넣어주면 동적 프록시 생성하고 반환한다.
 */
@Slf4j
public class JdkDynamicProxyTest {
    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // proxy가 어디에 생성되어야 할지 지정(classLoader), 두번째는 인터페이스를 넣어줘야 한다. 배열인 이유는 인터페이스가 여러 개일 구현할 수도 있기 때문
        // 반환 타입이 Object라서 캐스팅 가능
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);

        proxy.call(); // AInterface에 call()이 있으므로 호출
        log.info("targetClass={}", target.getClass()); // targetClass=class hello.proxy.jdkdynamic.code.AImpl
        log.info("proxyClass={}", proxy.getClass()); // proxyClass=class jdk.proxy3.$Proxy12
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // proxy가 어디에 생성되어야 할지 지정(classLoader), 두번째는 인터페이스를 넣어줘야 한다. 배열인 이유는 인터페이스가 여러 개일 구현할 수도 있기 때문
        // 반환 타입이 Object라서 캐스팅 가능
        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call(); // AInterface에 call()이 있으므로 호출
        log.info("targetClass={}", target.getClass()); // targetClass=class hello.proxy.jdkdynamic.code.BImpl
        log.info("proxyClass={}", proxy.getClass()); // proxyClass=class jdk.proxy3.$Proxy12
    }
}
