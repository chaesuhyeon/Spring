package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

// 스프링 빈으로 등록 필수
@Slf4j
@Aspect
public class AspectV4Pointcut {

    // OrderService, OrderRepository에 적용
    @Around("hello.aop.order.aop.Pointcuts.allOrder()") // 포인트 컷
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable { // 어드바이스
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed(); // target 호출
    }

    // hello.aop.order 패키지와 하위 패키지 이면서 클래스 이름 패턴이 *Service
    // XxxService 또는 *Servi*와 같은 패턴도 가능
    // OrderService 에만 적용
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()") // 포인트 컷 조합
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable { // 어드바이스

        try {
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature()); // join point 시그니처
            Object result = joinPoint.proceed();
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature()); // join point 시그니처

            return result;
        } catch (Exception e){
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature()); // join point 시그니처
            throw e;
        } finally {
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature()); // join point 시그니처
        }
    }
}
