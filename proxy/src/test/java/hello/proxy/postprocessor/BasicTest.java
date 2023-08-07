package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {

    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class); // beanA라는 이름으로 스프링 빈으로 등록한다.

        // A는 빈으로 등록된다.
        A a = applicationContext.getBean("beanA", A.class);
        a.helloA();

        // B는 스프링 빈으로 등록되지 않는다. (BasicConfig.class에서 정의를 하지 않았으므로)
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> {
            B b = applicationContext.getBean(B.class);
        });

    }

    @Slf4j
    @Configuration
    static class BasicConfig {
        @Bean(name = "beanA")
         public A a() {
            return new A();
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
    }}
