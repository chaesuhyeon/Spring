package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form , BindingResult bindingResult , HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail" , "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리 TODO
        //쿠키에 시간 정보를 주지 않으면 세션쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId())); // 로그인에 성공하면 쿠키 생성
        response.addCookie(idCookie); // 생성한 쿠키를 HttpServletResponse에 담는다. 쿠키 이름은 memberId이고, 이 값은 회원의 id를 담아둔다. 웹브라우저는 종료 전까지 회원의 id를 서버에 계속 보내줄 것이다.

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response , "memberId");
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response , String cookieName) {
        Cookie cookie = new Cookie(cookieName , null);
        cookie.setMaxAge(0); // 쿠키 시간을 0으로 하면 삭제
        response.addCookie(cookie);
    }
}
