package hello.core.discount;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
// @Qualifier("mainDiscountPolicy") // 컴파일할때 문자체크를 해주지 않아서 오타가 나도 컴파일 오류가 발생하지 않음. --> 직접 만든 MainDiscountPolicy 어노테이션 사용
//@Primary
@MainDiscountPolicy
public class RateDiscountPolicy implements  DiscountPolicy{
    private int discountPercent = 10;


    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100; // VIP면 10% 할인
        } else {
            return 0; // VIP가 아니면 0원 할인
        }
    }
}
