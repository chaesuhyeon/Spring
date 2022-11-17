package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
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
//    public String home() {
//        return "home";
//    }

    //@GetMapping("/")
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

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){

        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);


        // 로그인
        if (member == null){
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";

    }

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model){

        HttpSession session = request.getSession(false);

        // 로그인
        if (session == null){ // 세션이 없으면 home으로 보냄
            return "home";
        }

        //세션에 저장된 회원 정보 조회
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}