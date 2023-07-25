package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * MethodInterceptor를 구현. 패키지  org.aopalliance.intercept.MethodInterceptor 인거 주의!!
 * Object result = invocation.proceed()
 *      invocation.proceed()를 호출하면 target 클래스를 호출하고 그 결과를 받는다.
 *      기존에 보았던 코드들과 다르게 target 클래스의 정보가 보디지 않는다. target 클래스의 정보는 MethodInvocation invocation 안에 포함되어 있따.
 *      그 이유는 프록시 팩토리 단계에서 이미 target 정보를 파라미터로 전달받기 때문이다.
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = invocation.proceed();// 알아서 target을 찾아서 실제 method를 호출해준다.

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime = {}" , resultTime);

        return result;
    }
}
