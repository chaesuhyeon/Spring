package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }

    // @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginForm == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다"); // 그냥 reject이면 글로벌 validation (field 아님)
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 브라우저 종료시 로그아웃이 되길 기대하므로 세션쿠키를 사용한다. (만료 날짜 생략하는 쿠키)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logout(HttpServletResponse response) {

        // cookie를 지우는 방법은 시간을 0으로 하면 된다.
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginForm == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다"); // 그냥 reject이면 글로벌 validation (field 아님)
            return "login/loginForm";
        }

        // 로그인 성공 처리

        // 세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(loginMember, response);
        return "redirect:/";
    }

   // @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginForm == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다"); // 그냥 reject이면 글로벌 validation (field 아님)
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 세션이 있으면 있는 세션을 반환하고 없으면 신규 세션을 생성한다.
        // 세션을 생성하고 싶다면 request.getSession(true)를 사용하면 된다.(default 값은 true)
        // request.getSession(false)로 하면 세션이 있으면 기존 세션을 반환하지만 세션이 없으면 세션을 생성하지 않고 null을 반환
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없어도 새로 생성하면 안되기 때문에 false로 줌
        if(session != null) {
            session.invalidate();
        }
        
        return "redirect:/";
    }


    private static void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
