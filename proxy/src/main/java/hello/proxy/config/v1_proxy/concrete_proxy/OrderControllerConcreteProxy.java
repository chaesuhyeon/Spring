package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

/**
 * super(null)에 대해서는 OrderServiceConcreteProxy 참고
 */
public class OrderControllerConcreteProxy extends OrderControllerV2 {
    private final OrderControllerV2 target;
    private final LogTrace logTrace;

    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderController.request()");

            // 실제 target 호출
            String result = target.request(itemId);
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e; // 기존 흐름을 변동하면 안되기 때문에 예외를 던저준다.
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
