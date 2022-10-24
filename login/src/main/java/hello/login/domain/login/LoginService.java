package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     *
     * @return null 이면 로그인 실패
     */
    public Member login(String loginId , String password){
/*        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);

        Member member = findMemberOptional.get();

        if(member.getPassword().equals(password)){
            return member;
        } else {
            return null;
        }*/

        return memberRepository.findByLoginId(loginId) // 회원을 조회한 다음
                .filter(m -> m.getPassword().equals(password)) // 파라미터로 넘어온 password와 비교해서 같으면 회원을 반환하고
                .orElse(null); // password가 다르면 null을 반환

    }
}
