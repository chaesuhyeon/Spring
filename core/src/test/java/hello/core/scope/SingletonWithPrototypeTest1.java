package hello.core.scope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1); 

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class); // prototypeBean1과 다른 빈 호출
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1); // count가 다시 0부터 시작해서 +1 해서 1이 됨

    }

    @Test
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class); 
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);


        ClientBean clientBean2 = ac.getBean(ClientBean.class); // clientBean1과 같은 빈 호출 
        int count2 = clientBean2.logic();
//        assertThat(count2).isEqualTo(2); // 기존 count = 1의 값에 +1 해서 2가 됨
        assertThat(count2).isEqualTo(1); // Provider이용

    }

    @Scope("singleton")
    static class ClientBean {
        //private final PrototypeBean prototypeBean; // 생성 시점에 주입

        @Autowired
        private Provider<PrototypeBean> prototypeBeansProvider;
//        private ObjectProvider<PrototypeBean> prototypeBeansProvider;

//        @Autowired
//        public ClientBean(PrototypeBean prototypeBean) {
//            this.prototypeBean = prototypeBean;
//        }


        public int logic(){
            PrototypeBean prototypeBean = prototypeBeansProvider.get();
//            PrototypeBean prototypeBean = prototypeBeansProvider.getObject();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
//            prototypeBean.addCount();
//            int count = prototypeBean.getCount();
            return count;
        }
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount(){
            count ++;
        }

        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init" + this);
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }


}
