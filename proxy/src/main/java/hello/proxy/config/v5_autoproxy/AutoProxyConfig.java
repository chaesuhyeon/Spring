package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 빈 후처리기(AnnotationAwareAspectJAutoProxyCreator)는 이미 자동으로 등록 되어 있음 (aop라이브러리 추가)
 */
@Configuration
@Import({AppV1Config.class, AppV2Config.class}) // v3는 이미 컴포넌트의 대상이기 때문에 자동으로 스프링 빈으로 등록된다. (별도의 설정파일 필요 없음)
public class AutoProxyConfig {

    /**
     * 어드바이저는 bean으로 등록되어 있어야 한다.
     */
//    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        // pointcut (noLog인 경우에는 로그 x)
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    /**
     * 패키지로만 포인트 컷을 적용해서 no-log일때도 로그가 찍힌다.
     */
//    @Bean
    public Advisor advisor2(LogTrace logTrace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))"); //hello.proxy.app 이 위치에 있어야 프록시 적용 대상이 된다.

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))"); //hello.proxy.app 이 위치  + noLog가 아닌 경우에 있어야 프록시 적용 대상이 된다.

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
