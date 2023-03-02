package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //readOnly = true로 주면 jpa가 조회하는 곳에서는 성능을 조금 더 최적화 해준다 . //jpa의 모든 데이터 변경이나 로직들은 가급적 트랜잭션 안에서 다 실행되어야 함
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional // 위에 적어줬는데 밑에 한번 더 선언 해주면 이부분이 우선권을 가져서 먼저 적용되고 나머지는 class 레벨에서 선언해준 @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // 회원 전체 조회
    //@Transactional(readOnly = true) //readOnly = true로 주면 jpa가 조회하는 곳에서는 성능을 조금 더 최적화 해준다
    public List<Member> findMembers(){
        return memberRepository.fineAll();
    }

    //@Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    /**
     * 중복회원 검증 메서드 
     */
    private void validateDuplicateMember(Member member) {
        List<Member> fineMembers = memberRepository.findByName(member.getName());
        if(!fineMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id); // 트랜잭션이 있는 상태에서 조회하면 영속성 상태 객체를 반환
        member.setName(name); // 변경 감지에 의한 수정 (따로 repository에 있는 save 메서드 사용을 하지 않아도 변경감지에 의해 수정이 됨)
    }
}
