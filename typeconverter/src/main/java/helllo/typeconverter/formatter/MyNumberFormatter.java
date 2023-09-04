package helllo.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * String은 기본으로 변환이 되기 때문에 String을 제외한 타입을 Formatter< !! > 여기에 넣어주기
 */
@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

    /**
     * parse : 문자를 숫자로 변환
     * 참고로 Number 타입은 Integer, Long과 같은 숫자 타입의 부모
     * return은 Long 타입으로 된다.
     */
    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text={}, locale={}", text, locale);
        
        // "1,000" -> 1000
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text);
    }


    @Override
    public String print(Number object, Locale locale) {
        log.info("object={}, locale={}", object, locale);

        NumberFormat instance = NumberFormat.getInstance(locale);
        return instance.format(object);
    }
}
