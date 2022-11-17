package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter  실행");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);// Login 애노테이션이 파라미터에 있는지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType()); // 파라미터로 넘어온 타입이 Member인지 확인
        
        return hasLoginAnnotation && hasMemberType; // 둘다 true일 경우에 true 반환
    }

    // supportsParameter 가 true를 반환해야 실행
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        //session이 null이면 null 반환
        if(session == null){
            return null;
        }
        // session이 null이 아니면 member반환
        Object member = session.getAttribute(SessionConst.LOGIN_MEMBER);

        return member;
    }
}
