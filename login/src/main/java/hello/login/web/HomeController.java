package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

//    @GetMapping("/")
//    public String home() {
//        return "home";
//    }

    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId" , required = false) Long memberId , Model model){
        // CookieValue -> 저장된 쿠키 받아옴
        // required = false -> 로그인 하지 않은 사용자(쿠키값이 없는 사용자)도 들어와야하기 때문

        // 로그인 안한 경우
        if(memberId == null){
            return "home";
        }

        // 로그인 성공
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";

    }
}