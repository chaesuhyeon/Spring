package helllo.typeconverter;

import helllo.typeconverter.converter.IntegerToStringConverter;
import helllo.typeconverter.converter.IpPortToStringConverter;
import helllo.typeconverter.converter.StringToIntegerConverter;
import helllo.typeconverter.converter.StringToIpPortConverter;
import helllo.typeconverter.formatter.MyNumberFormatter;
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

        // 우선순위 때문에 주석처리한다. (MyNumberFormatter도 숫자 <-> 문자 이기 때문에 포맷터를 동작시키기 위해 주석처리 한다.)
/*        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());*/

        // 컨버터 등록
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        // 포맷터 등록
        registry.addFormatter(new MyNumberFormatter());
    }
}
