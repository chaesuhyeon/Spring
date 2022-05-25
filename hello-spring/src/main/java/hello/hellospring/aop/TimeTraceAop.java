package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect // 이걸 적어줘야 aop사용 가능
@Component
public class TimeTraceAop {
    @Around("execution(* hello.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
       long start = System.currentTimeMillis();
        System.out.println("START : "  +joinPoint.toString());
       try {
           // 다음 메서드로 진행
           return joinPoint.proceed();

        } finally {
           long finish = System.currentTimeMillis();
           long timeMs = finish - start;
           System.out.println("END : "  +joinPoint.toString() + " " + timeMs+"ms");
       }
    }
}
