package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage(){
        String result = ms.getMessage("hello", null, null);// 메세지(code), argument, locale
        //locale이 null이므로 basename에서 설정한 기본 이름 메시지 파일을 조회한다. basename으로 messages를 지정했으므로 (지정 안해도 default는 messages) messages.properties에서 조회
        
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    void notFoundMessageCode(){
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null)).isInstanceOf(NoSuchMessageException.class); 
        // messages.properties에 "no_code"라는 코드(메시지)가 없기 때문에 NoSuchMessageException 발생
    }

    @Test
    void notFoundMessageCodeDefaultMessage(){
        String result = ms.getMessage("no_code", null,"기본 메세지", null);// 메세지(code), argument, default message,  locale

        assertThat(result).isEqualTo("기본 메세지"); // messages.properties에 "no_code"라는 코드(메시지)가 없을 때 defaultMessage 적용
    }

    @Test
    void argumentMessage(){ // 매개변수 넘기기
        String message = ms.getMessage("hello.name" , new Object[]{"Spring"} , null); // argument는 Object 배열로 해야함!
        assertThat(message).isEqualTo("안녕 Spring");
    }

    @Test
    void defaultLang(){
        assertThat(ms.getMessage("hello" , null , null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello" , null , Locale.KOREA)).isEqualTo("안녕");
   }

    @Test
    void enLang(){
        assertThat(ms.getMessage("hello" , null , Locale.ENGLISH)).isEqualTo("hello");
    }
}
