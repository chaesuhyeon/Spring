package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

public class PrototypeTest {

    @Test
    void prototypeBeanFind(){

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);

        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);

        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);
        assertThat(prototypeBean1).isNotSameAs(prototypeBean2); // 프로토 타입은 다른 빈 반환(싱글톤타입과 다른 점)

        ac.close();
        prototypeBean1.destory(); // 종료메서드 직접 호출
    }

    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destory(){
            System.out.println("PrototypeBean.destory");
        }
    }
}

//find prototypeBean1
//PrototypeBean.init  초기화 메서드 실행

//find prototypeBean2
//PrototypeBean.init 초기화 메서드 실행

//prototypeBean1 = hello.core.scope.PrototypeTest$PrototypeBean@4b41dd5c
//prototypeBean2 = hello.core.scope.PrototypeTest$PrototypeBean@3b96c42e

// destory 호출 안됨 