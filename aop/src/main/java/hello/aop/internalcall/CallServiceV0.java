package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV0 {

    /**
     * CallServiceV0.external() 을 호출하면 내부에서 internal()이라는 자기 자신의 메서드를 호출한다.
     */
    public void external() {
        log.info("call external");
        internal(); // 내부 메서드 호출(this.internal()) --> 자바 언어에서 메서드를 호출할 때 대상을 지정하지 않으면 앞에 자기 자신의 인스턴스를 뜻하는 this가 붙게 된다.
    }

    public void internal() {
        log.info("call internal");

    }
}
