package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor // final이 붙은 것을 생성자로 만들어줌(이것을 사용함으로 인해서 밑에 생성자 주석처리)
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository ;
    private final DiscountPolicy discountPolicy ;


    // ------------------------------------ Lombok의 @RequiredArgsConstructor 사용으로 생성자 주석처리 -----------------------------

//    @Autowired // 스프링 Bean인데 생성자가 1개일경우  @Autowired 생략가능. 생략해도 알아서 의존관계 주입 해줌 (스프링 빈일경우에만 해당)
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        System.out.println("memberRepository = " + memberRepository); // @Autowired 주석 처리하고 AutoAppConfigTest 실행해봐서 값들어오는지 확인
        System.out.println("discountPolicy = " + discountPolicy); // @Autowired 주석 처리하고 AutoAppConfigTest 실행해봐서 값들어오는지 확인
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    // -------------------------------------------------------------------------------------------------------------------


    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
