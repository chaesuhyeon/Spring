package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0; // 프록시

    @Test
    void external() {
        log.info("target={}", callServiceV0.getClass()); // target=class hello.aop.internalcall.CallServiceV0$$EnhancerBySpringCGLIB$$4d2e7e35 (프록시인거 확인)
        callServiceV0.external();

        /**
         * [external()에서 직접 내부 호출한 internal은 aop적용 x]
         * aop=void hello.aop.internalcall.CallServiceV0.external()
         * call external
         * call internal
         */
    }

    @Test
    void internal() {
        callServiceV0.internal();

        /**
         * [프록시를 통한 외부 호출은 aop 적용]
         * aop=void hello.aop.internalcall.CallServiceV0.internal()
         * call internal
         */
    }
}