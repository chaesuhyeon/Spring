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
    public String home() {
        return "home";
    }

    /**
     * 로그인 안한 사용자도 home을 들어올 수 있어야 하기 때문에 required = false
     * cookie의 값은 String으로 저장했는데 Long으로 받을 수 있다. -> typeconverter가 자동으로 변환해준다.
     */
    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if (memberId == null) {
            return "home";
        }
        
        // 로그인
        Member loginMember = memberRepository.findById(memberId); // cookie는 있으나 db에는 값이 없는 경우에도 그냥 home으로 이동
        if(loginMember == null) {
            return "home";
        }

        // cookie에도 값이 있고 db에도 값이 있으면 model에 담아준다.
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}