package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.")
    void vip_o() {
        // given
        // VIP 등급의 회원이라면
        Member member = new Member(1L, "memberVIP", Grade.VIP);

        // when
        // 10000원을 주문했다면 10% 할인이 되어야 하기 때문에 1000이 반환될 것이다.
        int discount = discountPolicy.discount(member, 10000);

        // then
        // 1000원이 할인되어야 한다.
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP가 아니라면 할인이 적용되지 않아야 한다.")
    void vip_x() {
        // given
        // VIP 등급의 회원이 아니라면
        Member member = new Member(2L, "memberBASIC", Grade.BASIC);

        // when
        // 10000원을 주문했다면 10% 할인이 되어서는 안된다.
        int discount = discountPolicy.discount(member, 10000);

        // then
        // 0원이 할인되어야 한다.
        assertThat(discount).isEqualTo(0);
    }

}