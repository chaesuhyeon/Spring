package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) request; // ServletResponse 는 HttpServletRequest의 부모인데 기능이 별로 없어서 HttpServletRequest로 다운 캐스팅 해줌
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}] [{}]", uuid, requestURI);
            chain.doFilter(request, response); // 중요!! 다음 필터가 있으면 다음 필터가 호출되고, 없으면 서블릿이 호출됨
        } catch (Exception e){
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
