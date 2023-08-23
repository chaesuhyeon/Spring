package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {
    private CallServiceV1 callServiceV1; // 자기 자신 주입

    /**
     * setter로 의존관계를 주입해야한다.
     * 생성자 주입은 안된다. 자기 자신이 생성되지도 않았는데 생성자 주입을 한다는건 사이클에 문제가 있다.
     * 생성이 다 끝나면 생성된 자기 자신을 setter로 주입을 해준다.
     */
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) { // 프록시가 들어온다.
        log.info("callService setter = {}", callServiceV1.getClass()); // callService setter = class hello.aop.internalcall.CallServiceV1$$EnhancerBySpringCGLIB$$1c131d49
        this.callServiceV1 = callServiceV1; // 프록시가 의존관계 주입이 된다.
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); // 외부 메서드 호출
    }

    public void internal() {
        log.info("call internal");

    }
}
