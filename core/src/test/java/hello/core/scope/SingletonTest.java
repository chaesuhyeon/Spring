package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SingletonTest {

    @Test
    void singletonBeanFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletoneBean.class);
        SingletoneBean singletonBean1 = ac.getBean(SingletoneBean.class);
        SingletoneBean singletonBean2 = ac.getBean(SingletoneBean.class);

        System.out.println("singletonBean1 = " + singletonBean1);
        System.out.println("singletonBean2 = " + singletonBean2);
        Assertions.assertThat(singletonBean1).isSameAs(singletonBean2); // 같은 빈 출력(싱글톤은 같은 빈 반환)

        ac.close();
    }

    @Scope("singleton")
    static class SingletoneBean {

        @PostConstruct
        public void init(){
            System.out.println("SingletoneBean.init");
        }

        @PreDestroy
        public void destory(){
            System.out.println("SingletoneBean.destory");
        }

    }

}
