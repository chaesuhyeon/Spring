package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;

/**
 * new DefaultPOintcutAdvisor : Advisor 인터페이스의 가장 일반적인 구현체. 생성자를 통해 하나의 포인트컷과 하나의 어드바이스를 넣어주면 된다. 어드바이저는 하나의 포인트컷과 하나의 어드바이스로 구성된다.
 * Pointcut.TRUE : 항상 true를 반환하는 포인트컷
 * proxyFactory.addAdvisor(advisor) : 프록시 팩토리에 적용할 어드바이저를 저장한다.
 */
@Slf4j
public class AdvisorTest {

    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice()); // Pointcut.TRUE : 항상 참인 포인트컷
        proxyFactory.addAdvisor(advisor);

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
    }

    @Test
    @DisplayName("직접 만든 포인트컷")
    void advisorTest2() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointCut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
    }

    /**
     * Pointcut 인터페이스를 구현한다.
     */
    static class MyPointCut implements Pointcut {

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE; // 항상 true를 반환하는 classFilter 반환
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    /**
     * MethodMatcher 인터페이스를 구현한다.
     * matches() : 이 메서드에 method, targetClass 정보가 넘어온다. 이 정보로 advice를 적용할지 말지 판단할 수 있다.
     */
    static class MyMethodMatcher implements MethodMatcher{

        private String matchName = "save";

        @Override // 이 부분만 보면 됨
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(matchName); // 메서드 이름이 save 인 경우에 true를 반환하도록 로직 적용
            log.info("포인트 컷 호출 method={}  targetClasses={}", method.getName(), targetClass);
            log.info("포인트 컷 결과 result={}", result);
            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }
}
