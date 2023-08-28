package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy(){
        MemberServiceImpl target = new MemberServiceImpl(); // 구체 클래스와 인터페이스 둘다 있음
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.setProxyTargetClass(false); //JDK 동적 프록시

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // JDK 동적 프록시를 구현 클래스로 캐스팅 시도 실패, ClassCastException 에외 발생
        Assertions.assertThrows(ClassCastException.class, ()-> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) proxyFactory.getProxy();
        });

    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl(); // 구체 클래스와 인터페이스 둘다 있음
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.setProxyTargetClass(true); //CGLIB 동적 프록시

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // CGLIB 프록시를 구현 클래스로 캐스팅 시도 성공
        MemberServiceImpl castingMemberService = (MemberServiceImpl) proxyFactory.getProxy();
    }
}
