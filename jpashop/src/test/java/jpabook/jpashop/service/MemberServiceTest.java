package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryOld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional //jpa에서 같은 트랜잭션 안에서 pk값이 똑같으면 같은 영속성 컨텍스트에서 똑같은 애로 관리가 됨
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepositoryOld memberRepository;

    @Test
//    @Rollback(value = false) // @Transactional가 test에 있으면 rollback 해주기 때문에 rollback 을 안하고 싶다면 @Rollback(value=false)해주면 된다.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("Kim");

        //when
        Long savedId = memberService.join(member);

        //then
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);

        //memberService.join(member2); // 예외가 발생해야 한다!! / IllegalStateException: 이미 존재하는 회원입니다.

        //then

        //Junit5 이용할 때 exception 발생하는 경우 test
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());

        Assertions.fail("예외가 발생해야 한다."); // 이미 위에서 Exception이 발생해서 여기까지 오면 안된다. 여기까지 와도 test 실패
    }

}