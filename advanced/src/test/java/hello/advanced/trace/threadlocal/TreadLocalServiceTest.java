package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.TreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TreadLocalServiceTest {
    private TreadLocalService service = new TreadLocalService();


    @Test
    void field() {
        log.info("main start");

        // 동시성 문제는 여러개의 쓰레드가 병합이 될때 문제
        // 쓰레드 실행 로직
        Runnable userA = () -> {
            try {
                service.logic("userA");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable userB = () -> {
            try {
                service.logic("userB");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");

        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
//        sleep(2000); // 동시성 문제 발생 X
        sleep(100); // 동시성 문제 발생O
        threadB.start();

        sleep(3000); // 메인 쓰레드 종료 대기
        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}