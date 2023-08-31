package helllo.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * Integer를 String으로 변환하는 컨버터
 */
@Slf4j
public class IntegerToStringConverter implements Converter<Integer,String> {

    @Override
    public String convert(Integer source) {
        log.info("convert source={}", source);
        return String.valueOf(source);
    }
}
