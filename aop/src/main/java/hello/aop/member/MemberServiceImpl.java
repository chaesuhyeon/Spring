package hello.aop.member;

import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component /* 스프링 AOP를 하려면 스프링 빈으로 등록이 되어야 한다. */
public class MemberServiceImpl implements MemberService{

    @Override
    @MethodAop("test value")
    public String hello(String param) {
        return "ok";
    }

    // override 안하고 내부에서만 가지고 있는 method
    public String internal(String param) {
        return "ok";
    }
}
