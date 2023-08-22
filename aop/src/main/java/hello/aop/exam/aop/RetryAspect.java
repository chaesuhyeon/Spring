package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {
    /**
     * @Around를 써야한다. --> 재시도할 때 언제 내가 조인 포인트 proceed()를 호출할지 다시 결정해야하기 때문
     */
//    @Around("@annotation(hello.aop.exam.annotation.Retry)")
    @Around("@annotation(retry)") // 위에처럼 안적어줘도 아래 파라미터에 Retry retry를 적어주면 타입 정보가 들어간다.
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        log.info("[retry] {} retry={}", joinPoint.getSignature(), retry);
        
        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {
                log.info("[retry] try count={}/{}", retryCount, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e){
                exceptionHolder = e; // 예외를 잡고 있는다. 예외가 터져도 maxRetry 횟수만큼 for문이 진행된다. maxRetry횟수만큼  시도헸는데도 안되면
            }
        }

        throw exceptionHolder; // 마지막 예외가 던져진다.
    }

}
