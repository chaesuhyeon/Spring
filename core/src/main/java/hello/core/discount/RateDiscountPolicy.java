package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

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
