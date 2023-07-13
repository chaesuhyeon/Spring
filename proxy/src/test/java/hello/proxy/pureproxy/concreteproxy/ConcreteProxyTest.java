package hello.proxy.pureproxy.concreteproxy;

import hello.proxy.pureproxy.concreteproxy.code.ConcreteClient;
import hello.proxy.pureproxy.concreteproxy.code.ConcreteLogic;
import hello.proxy.pureproxy.concreteproxy.code.TimeProxy;
import org.junit.jupiter.api.Test;

public class ConcreteProxyTest {
    @Test
    void noProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient client = new ConcreteClient(concreteLogic);
        client.execute();
    }

    /**
     * concreteClient의 생성자에 concreteLogic이 아니라 timeProxy를 주입하는 부분이 핵심
     * ConcreteClient는 ConcreteLogic에 의존하는데, 다형성에 의해서 ConcreteLogic은 concreteLogic 뿐만 아니라 이것을 상속받아서 구현한 timeProxy도 들어갈 수 있다.
     */
    @Test
    void addProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        TimeProxy timeProxy = new TimeProxy(concreteLogic);
        ConcreteClient client = new ConcreteClient(timeProxy);
        client.execute();
    }
}
