package hello.login.web.session;


import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest(){

        //세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse(); // HttpServletResponse 같은 것은 인터페이스. -> 톰캣이 해주는데 톰캣 실행 안하므로.. 테스트 하기 위해 스프링에서 Mock으로 제공
        Member member = new Member();
        sessionManager.createSession(member,response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request); // 세션 remove
        Object expired = sessionManager.getSession(request); // 세션 조회
        assertThat(expired).isNull(); // Null인지 확인
    }


}