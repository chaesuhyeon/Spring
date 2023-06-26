package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 변하는 알고리즘은 Strategy 인터페이스를 구현
 */
@Slf4j
public class StrategyLogic2 implements Strategy{
    @Override
    public void call() {
        log.info("비즈니스 로직2 실행");
    }
}
