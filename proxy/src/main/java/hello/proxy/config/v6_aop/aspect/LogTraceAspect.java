package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

/**
 * @Aspect : 애노테이션 기반 프록시를 적용할 때 필요
 * @Around("execution(* hello.proxy.app..*(..))") : 포인트컷 표현식을 넣는다. (AspectJ 표현식으로 적어야 한다.)
 * @Around의 메서드는 어드바이스(Advice)가 된다.
 * ProceedingJoinPoint joinPoint : 어드바이스에서 살펴본 MethodInvocation invocation과 유사한 기능이며, 내부에 실제 호출 대상, 전달 인자, 그리고 어떤 객체과 어떤 메서드가 호출되었는지 정보가 포함되어 있다.
 * joinPoint.proceed() : 실제 호출 대상 (target)을 호출한다.
 */
@Slf4j
@Aspect
public class LogTraceAspect {
    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* hello.proxy.app..*(..))") // pointcut 조건 넣기
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { // 어드바이스 로직 작성
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            // 로직 호출
            Object result = joinPoint.proceed();
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e; // 기존 흐름을 변동하면 안되기 때문에 예외를 던저준다.
        }
    }
}
