package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * 로그인 안한 사용자도 home을 들어올 수 있어야 하기 때문에 required = false
     * cookie의 값은 String으로 저장했는데 Long으로 받을 수 있다. -> typeconverter가 자동으로 변환해준다.
     */
    //@GetMapping("/")
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

    /* 직접 만든 세션 사용 */
    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);

        // 로그인
        if(member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        // 홈화면에 들어왔을 때 session을 만들 의도가 없으므로 우선 false로 지정 (세션은 메모리를 사용하는 것이기 때문에 꼭 필요할 때만 생성해야 된다.)
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "home";
        }

        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션이 회원 데이터가 없으면 home
        if(member == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", member);
        return "loginHome";
    }

    /**
     *  SessionAttribute 는 session에서 LOGIN_MEMBER를 찾아서 자동으로 Member를 넣어준다.
     *  SessionAttribute는 세션을 생성하지 않는다.
     */
    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Model model) {

        // 세션이 회원 데이터가 없으면 home
        if(member == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", member);
        return "loginHome";
    }
}