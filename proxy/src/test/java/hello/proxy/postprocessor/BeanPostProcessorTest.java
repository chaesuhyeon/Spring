package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanPostProcessorTest {

    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class); // beanA라는 이름으로 스프링 빈으로 등록한다.

        // beanA 이름으로 B객체가  빈으로 등록된다.
        B b = applicationContext.getBean("beanA", B.class);
        b.helloB();

        // A는 스프링 빈으로 등록되지 않는다. (BeanPostProcessorConfig.class에서 정의를 하지 않았으므로)
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));

    }

    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig {
        @Bean(name = "beanA")
         public A a() {
            return new A();
        }

        @Bean
        public AtoBPostProcessor helloPostProcessor() {
            return new AtoBPostProcessor();
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }


    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    @Slf4j
    static class AtoBPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
           log.info("beanName={}, bean={}",beanName, bean );

           // 파라미터로 넘어온 빈 (bean) 객체가 A의 인스턴스이면 새로운 B 객체를 생성해서 반환한다. 여기서 A 대신에 반환된 값인 B가 스프링 컨테이너에 등록된다.
           if (bean instanceof A) {
               return new B();
           }

           return bean;
        }
    }

}
