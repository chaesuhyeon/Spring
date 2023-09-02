package helllo.typeconverter;

import helllo.typeconverter.converter.IntegerToStringConverter;
import helllo.typeconverter.converter.IpPortToStringConverter;
import helllo.typeconverter.converter.StringToIntegerConverter;
import helllo.typeconverter.converter.StringToIpPortConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 스프링은 내부에서 ConversionService를 제공한다.
 * WebMvcConfigure가 제공하는 addFormatters()를 사용해서 추가하고 싶은 컨버터를 등록하면 된다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /* addFormatters : converter의 확장된 버전 */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
    }
}
