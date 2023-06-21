package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldService {
    private String nameStore;

    public String logic(String name) throws InterruptedException {
        log.info("저장 name={} -> nameStore={}", name, nameStore);
        nameStore = name;
        sleep(1000);
        log.info("조회 nameStore={}" , nameStore);

        return nameStore;
    }

    private void sleep(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }

}
