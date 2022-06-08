package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // TreadA : A사용자 10000원 주문
        int userAPrice = statefulService1.order("userA", 10000); // 지역변수로 해결

        // TreadB : B사용자 10000원 주문
        int userBPrice = statefulService2.order("userB", 20000); // 지역변수로 해결

        // TreadA : 사용자A 주문 금액 조회
//        int price = statefulService1.getPrice();
        System.out.println("price = " + userAPrice); // 사용자 A가 주문해서 10000원의 값이 나와야하는데 그 사이에 B가 끼어들었으므로 20000만원이 결과값으로 나옴

//        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);

    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}