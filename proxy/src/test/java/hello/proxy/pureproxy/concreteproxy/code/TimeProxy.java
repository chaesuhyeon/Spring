package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic{

    // 프록시 이므로 실제 호출할 대상
    private ConcreteLogic concreteLogic;

    public TimeProxy(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    // 오버라이드 하면 다형성을 적용할 수 있다.
    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();

        String result = concreteLogic.operation();

        long endTime = System.currentTimeMillis();

        long resultTime = startTime - endTime;
        log.info("TimeDecorator 종료 resultTime={}ms", resultTime);

        return result;
    }
}
