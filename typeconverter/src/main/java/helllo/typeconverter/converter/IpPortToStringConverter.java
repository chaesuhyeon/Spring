package helllo.typeconverter.converter;

import helllo.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * IpPort 타입을 String으로 변환
 */
@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {
    @Override
    public String convert(IpPort source) {
        log.info("convert source={}", source);

        // IpPort 객체 -> "127.0.0.1:8080"
        return source.getIp() + ":" + source.getPort();
    }
}
