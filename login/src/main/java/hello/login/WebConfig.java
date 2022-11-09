package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // 우리가 만든 필터 지정
        filterRegistrationBean.setOrder(1); // 필터가 여러개 있을 수도 있으므로 순서 지정
        filterRegistrationBean.addUrlPatterns("/*"); // 어떤 URL 패턴에 필터를 적용할 것인지 지정

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter()); // 우리가 만든 필터 지정
        filterRegistrationBean.setOrder(2); // 필터가 여러개 있을 수도 있으므로 순서 지정
        filterRegistrationBean.addUrlPatterns("/*"); // 어떤 URL 패턴에 필터를 적용할 것인지 지정

        return filterRegistrationBean;
    }
}
