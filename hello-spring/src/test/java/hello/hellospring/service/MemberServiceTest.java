package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

//    MemberService memberService = new MemberService();
    MemberService memberService;

    MemoryMemberRepository memberRepository;
//    MemoryMemberRepository memoryMemberRepository = new MemoryMemberRepository();
    // 이렇게 new로 또 객체를 생성하면 MemberService에 있는 MemoryMemberRepository와 다른 객체로 테스트 하고 있는 것임

    @BeforeEach
    public  void beforEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }


    @AfterEach // 한 메서드의 실행이 끝날 때마다 작동함
    public  void afterEach() {
        memberRepository.clearStore();
        // 메서드가 끝날 때 마다 repository 비워줌
    }


    @Test
    void join() {
        // given
        Member member = new Member();
        member.setName("hello");

        // when
        Long saveId = memberService.join(member);

        // then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외(){
        // given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");


        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));// member2를 넣으면 예외가 터져야 함
        assertThat(e.getMessage()).isEqualTo("이미존재하는 회원 입니다.");
//        try{
//            memberService.join(member2);
//            fail();
//        } catch (IllegalStateException e) {
//            assertThat(e.getMessage()).isEqualTo("이미존재하는 회원 입니다.");
//        }



        // then
    }

    @Test
    void findMembers() {
    }

    @Test
    void fineOne() {
    }
}