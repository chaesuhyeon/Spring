package helllo.typeconverter.converter;

import helllo.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.*;

public class ConversionServiceTest {

    @Test
    void conversionService() {
        // 개별 컨버터 등록 (개별 컨버터들을 묶어서 관리해준다.)
        DefaultConversionService conversionService = new DefaultConversionService(); // ConversionService 인터페이스 구현체
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 컨버젼 서비스 사용
        assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10); // StringToIntegerConverter 적용
        assertThat(conversionService.convert(10, String.class)).isEqualTo("10"); // IntegerToStringConverter 적용
        assertThat(conversionService.convert("127.0.0.1:8080", IpPort.class)).isEqualTo(new IpPort("127.0.0.1", 8080)); // StringToIpPortConverter 적용
        assertThat(conversionService.convert(new IpPort("127.0.0.1", 8080), String.class)).isEqualTo("127.0.0.1:8080"); // IpPortToStringConverter 적용
    }
}
