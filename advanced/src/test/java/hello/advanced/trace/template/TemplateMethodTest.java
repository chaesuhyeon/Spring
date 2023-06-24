package hello.advanced.trace.template;

import hello.advanced.trace.template.code.AbstractTemplate;
import hello.advanced.trace.template.code.SubClassLogic1;
import hello.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * logic1()과 logic2()의 변하지 않는 부분 : 시간 측정
 * logic1()과 logic2()의 변하는 부분 : 비즈니스 로직
 */
@Slf4j
public class TemplateMethodTest {
    @Test
    public void templateMethodV0(){
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");

        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime =  endTime - startTime;
        log.info("resultTime = {}", resultTime);

    }

    private void logic2() {
        long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");

        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime =  endTime - startTime;
        log.info("resultTime = {}", resultTime);

    }

    /**
     * template1.excute()를 호출하면 템플릿 로직인 AbstractTemplate.excute()를 실행한다. 여기서 중간에 call() 메서드를 호출하는데
     * 이 부분이 오버라이딩 되어 있다.  따라서 현재 인스턴스인 SubClassLogic1 인스턴스의 SubClassLogic1.call() 메서드가 호출된다.
     */
    @DisplayName("템플릿 메서드 패턴 적용")
    @Test
    public void templateMethodV1(){
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }

    @DisplayName("템플릿 메서드 패턴 적용 - 익명 내부 클래스 사용")
    @Test
    public void templateMethodV2(){
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직1 실행");
            }
        };
        log.info("클래스 이름1={}" , template1.getClass());
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직2 실행");
            }
        };
        log.info("클래스 이름2={}" , template2.getClass());
        template2.execute();
    }
}
