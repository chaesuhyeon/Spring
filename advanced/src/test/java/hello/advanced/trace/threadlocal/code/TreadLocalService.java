package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

/**
 * TreadLocal 값 저장 : ThreadLlocal.set(xxx)
 * TreadLocal 값 조회 : ThreadLlocal.get()
 * TreadLocal 값 제거 : ThreadLlocal.remove()
 * 해당 쓰레드가 쓰레드 로컬을 모두 사용하고 나면 ThreadLocal.remove()를 호출해서 쓰레드 로컬에 저장된 값을 제거해주어야 한다.
 */
@Slf4j
public class TreadLocalService {
    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String logic(String name) throws InterruptedException {

        log.info("저장 name={} -> nameStore={}", name, nameStore.get());
        nameStore.set(name); // 값 셋팅

        sleep(1000);
        log.info("조회 nameStore={}" , nameStore.get());

        return nameStore.get(); // 값 조회
    }

    private void sleep(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }

}
