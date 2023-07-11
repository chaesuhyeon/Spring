package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

/**
 * 프록시는 실제 객체를 호출할 수 있어야 한다.
 */
@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {

    private final OrderControllerV1 target;
    private final LogTrace logTrace;


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
