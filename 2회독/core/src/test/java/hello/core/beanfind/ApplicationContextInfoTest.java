package hello.core.beanfind;

import hello.core.AppConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName); // 타입을 모르기 때문에 Object로 반환이 된다.

            System.out.println("name = " + beanDefinitionName + " object = " + bean);

            /*
                name = org.springframework.context.annotation.internalConfigurationAnnotationProcessor object = org.springframework.context.annotation.ConfigurationClassPostProcessor@3d08f3f5
                name = org.springframework.context.annotation.internalAutowiredAnnotationProcessor object = org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@7b139eab
                name = org.springframework.context.annotation.internalCommonAnnotationProcessor object = org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@4e76dac
                name = org.springframework.context.event.internalEventListenerProcessor object = org.springframework.context.event.EventListenerMethodProcessor@611df6e3
                name = org.springframework.context.event.internalEventListenerFactory object = org.springframework.context.event.DefaultEventListenerFactory@5f2f577
                name = appConfig object = hello.core.AppConfig$$SpringCGLIB$$0@6273c5a4
                name = memberService object = hello.core.member.MemberServiceImpl@5d465e4b
                name = orderService object = hello.core.order.OrderServiceImpl@53e211ee
                name = memberRepository object = hello.core.member.MemoryMemberRepository@41a90fa8
                name = discountPolicy object = hello.core.discount.RateDiscountPolicy@3d8bbcdc
            */
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName); // getBeanDefinition -> bean에 대한 meta data 정보

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) { // ROLE_APPLICATION : 직접 등록한 애플리케이션 빈
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + "object = " + bean);
            }
        }

    }
}
