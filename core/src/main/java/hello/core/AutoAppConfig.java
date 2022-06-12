package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(

        // 모든 자바 클래스를 다 컴포넌트 스캔하면 시간이 오래걸리므로, 필요한 위치부터 탐색하도록 시작 위치 지정할 수 있음 ->  basePackages
//        basePackages = "hello.core",

        // AutoAppConfig.class의 패키지인 hello.core가 시작위치로 지정됨
//        basePackageClasses = AutoAppConfig.class,

        excludeFilters= @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
        // 자동으로 스프링빈으로 등록해야하는데 그 중에서 뺄 것을 지정해줌
        // Configuration annotation 붙은 애를 빼줌 (AppConfig와 충돌나는 것을 방지하기 위해서)
)
public class AutoAppConfig {

}
