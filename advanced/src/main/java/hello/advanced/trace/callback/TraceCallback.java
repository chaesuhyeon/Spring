package hello.advanced.trace.callback;

/**
 * 타입이 다 다를 수 있으므로 제네릭 사용
 */
public interface TraceCallback<T> {
    T call();
}
