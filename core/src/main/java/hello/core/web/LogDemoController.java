package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LogDemoController {
    private final LogDemoService logDemoService;
    private final MyLogger myLogger;
//    private final ObjectProvider<MyLogger> myLoggerProvider;


    @RequestMapping("log-demo")
    @ResponseBody // 화면 없는 경우 문자로 바로 반환하고 싶을 때
    public String logDemo(HttpServletRequest request) throws InterruptedException { // HttpServletRequest 사용하면 고객 요청 정보 받을 수 있음

        String requestURL = request.getRequestURL().toString();
//        MyLogger myLogger = myLoggerProvider.getObject();

        System.out.println("myLogger = " + myLogger.getClass());
        myLogger.setRequestURL(requestURL);
        myLogger.log("controller test");
        Thread.sleep(1000);
        logDemoService.logic("testId");

        return "OK";
    }
}
