package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.*;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();  // MessageCodesResolver : 인터페이스  / DefaultMessageCodesResolver : 구현체

    @Test
    void messageCodesResolverObject(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item"); // 여러개 나옴 resolveMessageCodes(errorCode , objectName)

// 자세한 것이 1순위
//        messageCode = required.item
//        messageCode = required

        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);// resolveMessageCodes(errorCode , objectName , field , fieldType)
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

//        messageCode = required.item.itemName
//        messageCode = required.itemName
//        messageCode = required.java.lang.String
//        messageCode = required
        assertThat(messageCodes).containsExactly("required.item.itemName","required.itemName", "required.java.lang.String" ,"required");

    }
}
