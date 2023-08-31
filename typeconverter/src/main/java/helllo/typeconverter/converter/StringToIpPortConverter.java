package helllo.typeconverter.converter;

import helllo.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * String을 IpPort 타입으로 변환
 */
@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {
    @Override
    public IpPort convert(String source) {
        log.info("convert sour={}", source);

        //"127.0.0.1:8080"
        String[] split = source.split(":");
        String ip = split[0]; // "127.0.0.1"
        int port = Integer.parseInt(split[1]);//8080

        return new IpPort(ip, port);
    }
}
