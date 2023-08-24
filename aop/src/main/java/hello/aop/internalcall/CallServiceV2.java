package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 지연 조회
 */
@Slf4j
@Component
public class CallServiceV2 {

    private final ObjectProvider<CallServiceV2> callServiceProvider;

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }

    /**
     * 생성자 주입 (생성자가 하나이므로 생성자 주입 가능)
     */


    public void external() {
        log.info("call external");
        CallServiceV2 callServiceV2 = callServiceProvider.getObject(); // 지연 조회
        callServiceV2.internal(); // 외부 메서드 호출
    }

    public void internal() {
        log.info("call internal");

    }
}
