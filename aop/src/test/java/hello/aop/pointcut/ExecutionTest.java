package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ExecutionTest {
    // 포인트컷 표현식을 처리해주는 class
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class); // String.class는 hello 메소드의 파라미터 타입

    }

    @Test
    void printMethod() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }

    /**
     * [매칭 조건]
     * 접근제어자? : public
     * 반환 타입 : String
     * 선언 타입? : hello.aop.member.MemberServiceImpl
     * 메서드 이름 : hello
     * 파라미터 : (String)
     * 예외? : 생략
     */
    @Test
    void exactMatch(){  // 가장 정확한 포인트컷
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * [매칭 조건]
     * ?부분은 생략 가능
     * *는 아무 값이나 들어와도 됨
     * 접근제어자? : 생략
     * 반환 타입 : *
     * 선언 타입? : 생략
     * 메서드 이름 : *
     * 파라미터 : (..) --> ..은 파라미터의 타입이나 파라미터 수가 상관 없다는 뜻
     * 예외? : 생략
     */
    @Test
    void allMatch() { // 가장 많이 생략한 포인트컷
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }


    @Test
    void nameMatch() { // 메소드 이름 매치 포인트컷
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }


    @Test
    void nameMatchStar1() { // 메소드 이름 패턴 매치 포인트컷1
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar2() { // 메소드 이름 패턴 매치 포인트컷2
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFalse() { // 메소드 이름 패턴 매치 실패
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch1() { // 패키지 관련 가장 정확한 포인트컷
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2() { // 패키지 관련 포인트컷
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactFalse() { // 패키지 매치 실패
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.*.*(..))"); // hello.aop.member가 아닌 hello.aop이기 때문에 매치되는게 없음 -> ..으로 적어줘야 하위패키지까지 가능하다. hello.aop..*.*
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactSubPackage1() { // 하위 패키지 관련 포인트컷1
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.member..*(..))"); // member와 그 하위 패키지까지 가능 -> ..으로 적어줘야 하위패키지까지 가능하다.
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactSubPackage2() { // 하위 패키지 관련 포인트컷2
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop..*.*(..))"); // aop와 그 하위 패키지까지 가능
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMatch() { // 타입 매칭 포인트컷
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))"); // 타입 정보가 정확하게 일치
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException { // 타입 매칭 포인트컷
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))"); // 자식 타입
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class); // 자식 타입의 다른 메서드 (부모에서 오버라이드 하지 않은 메서드)

        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException { // 타입 매칭 포인트컷
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))"); // 부모 타입
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class); // 자식 타입의 다른 메서드 (부모에서 오버라이드 하지 않은 메서드)

        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse(); // 부모타입에서 선언한 메서드만 가능
    }

    // String 타입의 파라미터 허용
    // (String)
    @Test
    void argsMatch() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 파라미터가 없어야 함
    // ()
    @Test
    void argsMatchNoArgs() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *())"); // hello()에는 String 파라미터가 있기 때문에 실패한다.
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    // 정확히 하나의 파라미터 허용, 모든 타입 허용
    // (Xxx)
    @Test
    void argsMatchStar() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    // (), (Xxx), (Xxx, Xxx)
    @Test
    void argsMatchAll() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // String 타입으로 시작, 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    // (String), (String), (String, Xxx, Xxx)
    @Test
    void argsMatchComplex() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
