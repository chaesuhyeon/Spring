package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 필드에 전략을 보관하는 방식
 * ContextV1은 변하지 않는 로직을 가지고 있는 템플릿 코드
 * Context는 내부에 Strategy strategy 필드를 가지고 있다. 이는 필드에 변하는 부분인 Strategy 의 구현체를 주입하면된다.
 * 전략 패턴의 핵심은 Context는 Strategy 인터페이스에만 의존한다는 것이다.
 * 덕분에 Strategy 구현체를 변경하거나 새로 만들어도  Context 코드에는 영향을 주지 않는다.
 */
@Slf4j
public class ContextV1 {
    private Strategy strategy;

    // 생성자 주입
    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute(){
        long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행 (변하는 부분)
        strategy.call(); // 위임

        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime =  endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }
}
