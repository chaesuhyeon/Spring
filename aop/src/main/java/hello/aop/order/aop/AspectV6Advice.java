package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;

// 스프링 빈으로 등록 필수
@Slf4j
@Aspect
public class AspectV6Advice {

    // hello.aop.order 패키지와 하위 패키지 이면서 클래스 이름 패턴이 *Service
    // XxxService 또는 *Servi*와 같은 패턴도 가능
    // OrderService 에만 적용
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()") // 포인트 컷 조합
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable { // 어드바이스

        try {
            // @Before
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature()); // join point 시그니처
            Object result = joinPoint.proceed();

            // @AfterReturning
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature()); // join point 시그니처
            return result;
        } catch (Exception e){
            // @AfterThrowing
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature()); // join point 시그니처
            throw e;
        } finally {
            // @After
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature()); // join point 시그니처
        }
    }

    // join point 실행전에 실행됨 --> join point도 실행 해줌
    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint){
        log.info("[before] {}" , joinPoint.getSignature());

        /**
         * hello.aop.order.aop.AspectV6Advice       : [before] void hello.aop.order.OrderService.orderItem(String)
         * hello.aop.order.OrderService             : [orderService] 실행
         * hello.aop.order.OrderRepository          : [orderRepository] 실행
         */
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result){ // returning에 적은 값이랑 이름 일치
        log.info("[return] {} return={}" , joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex){
        log.info("[return] {} message={}" , joinPoint.getSignature(), ex.getMessage());
    }

    // finally같은 느낌
    @After("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint){
        log.info("[after] {}" , joinPoint.getSignature());
    }
}

/**
 * [트랜잭션 시작] void hello.aop.order.OrderService.orderItem(String)
 * [before] void hello.aop.order.OrderService.orderItem(String)
 * [orderService] 실행
 * [orderRepository] 실행
 * [return] void hello.aop.order.OrderService.orderItem(String) return=null
 * [after] void hello.aop.order.OrderService.orderItem(String)
 * [트랜잭션 커밋] void hello.aop.order.OrderService.orderItem(String)
 * [리소스 릴리즈] void hello.aop.order.OrderService.orderItem(String)
 */