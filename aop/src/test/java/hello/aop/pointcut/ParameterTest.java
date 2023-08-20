package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@Slf4j
@Import(ParameterTest.ParameterAspect.class) // 빈 등록
@SpringBootTest
public class ParameterTest {
    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberServiceProxy={}", memberService.getClass());
        // memberServiceProxy=class hello.aop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$f66c7a2b

        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {
        @Pointcut("execution(* hello.aop.member..*.*(..))") /* member 패키지와 하위 패키지까지 */
        private void allMember() {}


        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];

            log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), arg1); // arg1에 helloA가 잘 들어온것 확인
            // [logArgs1]String hello.aop.member.MemberServiceImpl.hello(String), arg=helloA

            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg, ..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable { // @Around안에 선언된 arg와 이름이 같아야 함

            log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg); // arg에 helloA가 잘 들어온것 확인
//            [logArgs2]String hello.aop.member.MemberServiceImpl.hello(String), arg=helloA

            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg, ..)") // @Before은 타겟의 로직을 자동으로 호출 해준다. (proceed() 호출 필요x)
        public void logArgs3(String arg) { // 이부분이 String으로 선언되어 있으면 args(String, ..) 과 같다. 파라미터 타입이 String이 아닌 것은 파라미터가 안들어온다.
            log.info("[logArgs3] arg={}", arg); // arg에 helloA가 잘 들어온것 확인
//            [logArgs3] arg=helloA
        }

        @Before("allMember() && this(obj)") // this 사용 --> 실제 스프링 컨테이너에 올라간 빈이 들어옴 (프록시)
        public void thisArgs(JoinPoint joinPoint, MemberService obj) { // @Before에서 JoinPoint 사용해도, 안해도 됨
            log.info("[this]{}, obj={}]", joinPoint.getSignature(), obj.getClass());
            // [this]String hello.aop.member.MemberServiceImpl.hello(String), obj=class hello.aop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$73929787] (프록시)
        }

        @Before("allMember() && target(obj)") // target 사용 --> 실제 대상 구현체가 들어옴
        public void targetArgs(JoinPoint joinPoint, MemberService obj) { // @Before에서 JoinPoint 사용해도, 안해도 됨
            log.info("[target]{}, obj={}]", joinPoint.getSignature(), obj.getClass());
            // [target]String hello.aop.member.MemberServiceImpl.hello(String), obj=class hello.aop.member.MemberServiceImpl] (실제 대상 구현체)
        }

        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) { // @Before에서 JoinPoint 사용해도, 안해도 됨
            log.info("[@target]{}, annotation={}]", joinPoint.getSignature(), annotation);
            //[@target]String hello.aop.member.MemberServiceImpl.hello(String), annotation=@hello.aop.member.annotation.ClassAop()]
        }

        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) { // @Before에서 JoinPoint 사용해도, 안해도 됨
            log.info("[@within]{}, annotation={}]", joinPoint.getSignature(), annotation);
            //  [@within]String hello.aop.member.MemberServiceImpl.hello(String), annotation=@hello.aop.member.annotation.ClassAop()]
        }
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) { // @Before에서 JoinPoint 사용해도, 안해도 됨
            log.info("[@annotation]{}, annotation={}, annotationValue={}]", joinPoint.getSignature(), annotation, annotation.value());
            // [@annotation]String hello.aop.member.MemberServiceImpl.hello(String), annotation=@hello.aop.member.annotation.MethodAop(value="test value"), annotationValue=test value]
        }

    }
}
