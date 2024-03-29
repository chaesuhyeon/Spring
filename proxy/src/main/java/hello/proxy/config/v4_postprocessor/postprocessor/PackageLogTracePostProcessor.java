package hello.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 원본 객체를 프록시 객체로 변환하는 역할
 * 모든 스프링 bean들을 프록시로 적용하지 않고, 특정 패키지 하위에 있는 bean들만 프록시 적용
 */
@Slf4j
public class PackageLogTracePostProcessor implements BeanPostProcessor {
    private final String basePackage;
    private final Advisor advisor;

    public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    /**
     * bean의 초기화가 다 끝나고 나서 프록시를 적용할 것이기 때문에 AfterInitialization 사용
     *
     * 프록시 대상 여부 체크를 해야한다
     *    - 애플리케이션을 실행해서 로그를 확인해보면 알겠지만, 우리가 직접 등록한 스플이 빈들 뿐만 아니라 스프링 부트가 기본적으로 등록하는 수많은 빈들이 빈 후처리기에 넘어온다.
     *    - 그래서 어떤 빈을 프록시로 만들 것인지 기준이 필요하다. 여기서는 간단히 basePackage를 사용해서 특정 패키지를 기준으로 해당 패키지와 그 하위 패키지의 빈들을 프록시로 만들었다.
     *    - 스프링 부트가 기본으로 제공하는 빈 중에는 프록시 객첼르 만들 수 없는 빈들도 있다. 따라서 모든 객체를 프록시로 만들 경우 오류가 발생한다.
     *    - 이 때도 pointcut을 사용하면 효율적이다.
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("param beanName={} bean={}", beanName, bean.getClass());

        // 프록시 대상 여부 체크
        // 프록시 적용 대상이 아니면 원본 bean 객체를 그대로 진행
        String packageName = bean.getClass().getPackageName();
        if (!packageName.startsWith(basePackage)) {
            return bean;
        }

        // 프록시 대상이면 프록시를 만들어서 반환
        ProxyFactory proxyFactory = new ProxyFactory(bean);//  new ProxyFactory(target)
        proxyFactory.addAdvisor(advisor);

        Object proxy = proxyFactory.getProxy();
        log.info("create proxy: target={} proxy={}", bean.getClass(), proxy.getClass());

        return proxy;
    }
}
