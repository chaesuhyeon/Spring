package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * LogTraceBasicHandler는 InvocationHandler 인터페이스를 구현해서 JDK 동적 프록시에서 사용된다.
 */
public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target; // 프록시가 호출할 대상
    private final LogTrace logTrace;

    public LogTraceBasicHandler(Object target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    /**
     * method에는 호출되는 메서드 정보와 클래스 정보가 담겨있다.
     * method.getDeclaringClass().getSimpleName() : class 정보를 꺼냄
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TraceStatus status = null;

        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";// 원래 logTrace.begin("OrderController.request()") 이렇게 클래스명 + 메서드명 이렇게 출력이 됐기 때문에 범용적으로 사용하기 위함
            status = logTrace.begin(message);

            // 로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e; // 기존 흐름을 변동하면 안되기 때문에 예외를 던저준다.
        }
    }
}
