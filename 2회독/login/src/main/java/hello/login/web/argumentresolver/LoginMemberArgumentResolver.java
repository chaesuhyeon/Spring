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
        log.info("supportParameter 실행");
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);// 파라미터에 Login 애노테이션이 있는지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());// 파라미터 타입이 Member 타입인지 확인

        return hasLoginAnnotation && hasMemberType;
    }

    /**
     * supportsParameter의 return 값이 true일 때 실행
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session;

        if (request.getSession(false) == null) {
            return null;
        }

        return request.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
