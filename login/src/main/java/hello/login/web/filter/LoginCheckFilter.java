package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist={"/" , "/members/add" , "/login" , "/logout" , "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작{}", requestURI);

            if(isLoginCheckPath(requestURI)){ // 로그인을 체크해야하는 경로인가 (false여야 화이트 리스트에 없으므로 로그인 체크를 해야함)
                log.info("인증 체크 로직 실행{}" , requestURI);
                HttpSession session = httpRequest.getSession(false); // 로그인을 체크해야하는 경로이면 세션에 데이터가 있는지 검사
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) { // null이면 로그인을 안했다는 뜻

                    log.info("미인증 사용자 요청 {}" , requestURI);
                    //로그인으로 redirect
                    httpResponse.sendRedirect("login?redirectURL=" + requestURI); // 로그인 후 기존에 요청했던 URL로 redirect
                    return; // 미인증 사용자는 다음으로 진행하지 않고 끝!
                }
            }
            chain.doFilter(request, response); // 로그인한 사용자라면 다음 필터 실행
        } catch (Exception e){
            throw e; // 예외 로깅 가능하지만, 톰캣까지 예외를 보내줘야 함
        } finally {
            log.info("인증 체크 필터 종료 {}" , requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI); // (false여야 화이트 리스트에 없으므로 로그인 체크를 해야함)
    }
}
