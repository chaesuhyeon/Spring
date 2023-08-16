package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

// 스프링 빈으로 등록 필수
@Slf4j
@Aspect
public class AspectV2 {

    // hello.aop.order 패키지와 하위 패키지
    @Pointcut("execution(* hello.aop.order..*(..))") // 분리했으니 여러군데서 사용 가능
    private void allOrder(){} // pointcut signature

    @Around("allOrder()") // 포인트 컷
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable { // 어드바이스
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed(); // target 호출
    }
}

/**
 * 적용된 로그
 * 각 메서드가 실행되기 전에 doLog 라는 aop가 적용된 모습을 볼 수 있음
 * [log] void hello.aop.order.OrderService.orderItem(String)
 * [orderService] 실행
 * [log] String hello.aop.order.OrderRepository.save(String)
 * [orderRepository] 실행
 */