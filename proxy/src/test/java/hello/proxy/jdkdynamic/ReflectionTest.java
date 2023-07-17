package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    /**
     * 공통 로직 1과 공통 로직 2는 메서드 호출하는 부분만 다르고 다 동일하다.
     * 공통 로직 1과 공통 로직 2를 하나의 메서드로 뽑아서 합칠 수 있을까? -> 생각보다 어려움. 왜냐하면 호출하는 '메서드'가 다르기 때문  -> 동적으로 처리한다면 해결 가능
     * 이럴 때 사용하는 것이 리플랙션이다.
     * 리플렉션은 클래스나 메서드의 메타정보를 사용해서 동적으로 호출하는 메서드를 변경할 수 있다.
     */
    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 메서드가 다름
        log.info("result={}", result1);
        // 공통 로직1 종료

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB(); // 호출하는 메서드가 다름(나머지 코드는 동일)
        log.info("result={}", result2);
        // 공통 로직2 종료
    }

    /**
     *  Class.forName ~ : 클래스 메타 정보를 획득한다. 참고로 내부 클래스는 구분을 위해 $를 사용한다.
     *  classHello.getMethod("callA") : 해당 클래스의 callA 메서드의 메타 정보를 획득한다.
     *  methodCallA.invoke(target) : 획득한 메서드 메타정보로 실제 인스턴스의 메서드를 호출한다. -> 인스턴스를 넘겨주면서 해당 인스턴스의 call을 찾아서 실행한다. 여기서는 target의 callA() 메서드를 호출한다.
     *  그냥 target.callA() 이런식으로 호출하면 되지 메서드 정보를 획득해서 메서드를 호출하면 어떤 효과가 있을까? -> 메서드 정보를 동적으로 변경할 수 있다.
     *  기존의 callA(), callB() 메서드를 직접 호출하는 부분이 Method로 대체되었다.
     */
    @Test
    void reflection1() throws Exception {
        // 클래스 정보
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");// 내부에 있는건 $로 가져오기

        Hello target = new Hello();

        // callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1={}" , result1);

        // callA 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}" , result2);
    }

    @Test
    void reflection2() throws Exception {
        // 클래스 정보
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");// 내부에 있는건 $로 가져오기

        Hello target = new Hello();

        // callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA , target);

        // callA 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB , target);
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA(){
            log.info("callA");
            return "A";
        }

        public String callB(){
            log.info("callB");
            return "B";
        }
    }
}
