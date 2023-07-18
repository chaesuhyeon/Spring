package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    // 프록시는 항상 프록시를 호출할 대상이 있어야 한다.
    // Object를 사용함으로써 범용적으로 사용하겠다는 뜻
    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    /**
     * method에는 프록시에 실행될 메서드가 넘어온다.
     * args : method의 argument
     * method.invoke(target, args) : target 인스턴스의 메서드를 실행한다. args는 메서드 호출시 넘겨줄 인수
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = method.invoke(target, args);


        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime = {}" , resultTime);

        return result;
    }
}
