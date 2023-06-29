package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * Strategy를 파라미터로 전달 받는 방식
 * Strategy를 필드로 가지지 않는다. 대신에 Strategy를 execute(...)가 호출 될 때 마다 항상 파라미터로 받는다.
 */
@Slf4j
public class ContextV2 {

    public void execute(Strategy strategy){
        long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행 (변하는 부분)
        strategy.call(); // 위임

        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime =  endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }
}
