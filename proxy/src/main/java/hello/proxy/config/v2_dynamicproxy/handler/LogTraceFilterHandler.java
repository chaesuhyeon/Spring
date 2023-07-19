package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * LogTraceFilterHandler는 InvocationHandler 인터페이스를 구현해서 JDK 동적 프록시에서 사용된다.
 * LogTraceFilterHandler는 특정 메서드가 이름이 매치되는 경우에만 LogTrace 로직을 실행한다. 이름이 매치되지 않으면 실제 로직을 바로 호출한다.
 */
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target; // 프록시가 호출할 대상
    private final LogTrace logTrace;
    private final String[] patterns; // method 이름이 patterns에 정의된 이름일 때만 log를 남긴다.

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    /**
     * method에는 호출되는 메서드 정보와 클래스 정보가 담겨있다.
     * method.getDeclaringClass().getSimpleName() : class 정보를 꺼냄
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 메서드 이름 필터
        String methodName  = method.getName();// 메서드 이름 가져옴
        if(!PatternMatchUtils.simpleMatch(patterns, methodName)) { // methodName이 patterns에 매치가 안되면 실제를 호출해준다.
            return method.invoke(target, args); // 그냥 끝내면 실제 서비스가 호출이 안되기 때문에 심각한 장애 발생. 그러므로 실제를 호출해줌
        }

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
