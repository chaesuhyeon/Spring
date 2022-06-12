package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
        // 자동으로 스프링빈으로 등록해야하는데 그 중에서 뺄 것을 지정해줌
        // Configuration annotation 붙은 애를 빼줌 (AppConfig와 충돌나는 것을 방지하기 위해서)
)
public class AutoAppConfig {

}
