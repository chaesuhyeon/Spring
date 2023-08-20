package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * application.properties
 * spring.aop.proxy-target-class=true // CGLIB
 * spring.aop.proxy-target-class=false // JDK 동적 프록시
 */
@Slf4j
@Import(ThisTargetTest.ThisTargetAspect.class)
@SpringBootTest(properties = "spring.aop.proxy-target-class=true")
public class ThisTargetTest {
    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberServiceProxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ThisTargetAspect {
        //부모 타입 허용
        @Around("this(hello.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        //부모 타입 허용
        @Around("target(hello.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        //this: 스프링 AOP 프록시 객체 대상
        //JDK 동적 프록시는 인터페이스를 기반으로 생성되므로 구현 클래스를 알 수 없음
        //CGLIB 프록시는 구현 클래스를 기반으로 생성되므로 구현 클래스를 알 수 있음
        @Around("this(hello.aop.member.MemberServiceImpl)")
        public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        //target: 실제 target 객체 대상
        @Around("target(hello.aop.member.MemberServiceImpl)")
        public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}

/**
 * JDK 동적 프록시 사용
 * this Impl은 로그가 출력되지 않는다. jdk 동적 프록시를 사용중이기 때문에 this에서는 인터페이스만 존재한다(memberService) 따라서 memberServiceImpl을 가지고 있지 않기 때문에 출력되지 않는다.
 * target은 실제 대상을 가지고 있기 때문에 memberServiceImpl을 가지고 있고, this와 target둘다 부모 타입은 허용하기 때문에 memberService는 로그에 남는다.
 *  memberServiceProxy=class com.sun.proxy.$Proxy59 (JDK 동적 프록시 사용하는 것 확인)
 * [target-impl] String hello.aop.member.MemberService.hello(String)
 * [target-interface] String hello.aop.member.MemberService.hello(String)
 * [this-interface] String hello.aop.member.MemberService.hello(String)
 */

/**
 * CGLIB 사용
 * CGLIB는 구체클래스를 기반한 프록시이기 때문에 this는 memberServiceImpl을 가지고 있다. 그리고 부모타입도 허용하므로 impl과 부모인 memberService둘다 출력이 된다.
 * memberServiceProxy=class hello.aop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$f38a5c68 (CGLIB 사용하는 것 확인)
 * [target-impl] String hello.aop.member.MemberServiceImpl.hello(String)
 * [target-interface] String hello.aop.member.MemberServiceImpl.hello(String)
 * [this-impl] String hello.aop.member.MemberServiceImpl.hello(String)
 * [this-interface] String hello.aop.member.MemberServiceImpl.hello(String)
 */
