package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

/**
 * 클래스 기반 프록시의 단점
 * super(null) : 자바 기본 문법에 의해 자식 클래스를 생성할 때는 항상 super()로 부모 클래스의 생성자를 호출해야 한다. 이부분을 생략하면 기본 생성자가 호출된다.
 * 그런데 부모 클래스인 OrderserviceV2에는 기본 생성자가 없고, 생성자에서 파라미터 1개를 필수로 받는다. (OrderRepository)
 * 따라서 파라미터를 넣어서 super()를 호출해야한다.
 * 프록시는 부모 객체의 기능을 사용하지 않기 때문에 super(null)을 입력해도 된다.
 * 인터페이스 기반 프록시는 이런 고민을 하지 않아도 된다.
 */
public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        super(null ); // 자바 문법상 부모 생성자를 호출해야하는데, 단순히 프록시만 생성하려고 하기 때문에 부모 생성자를 굳이 주입할 필요 없음 -> null 대입
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderService.orderItem()");

            // 실제 target 호출
            target.orderItem(itemId);
            logTrace.end(status);

        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e; // 기존 흐름을 변동하면 안되기 때문에 예외를 던저준다.
        }
    }
}
