package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={} , age={}" , username, age);
        response.getWriter().write("ok");
    }

    @RequestMapping("/request-param-v2") // http://localhost:8080/request-param-v2?username=kim&age=10
    @ResponseBody // @Controller면서 반환타입이 String이면 view를 찾게되기 때문에, HTTP 메세지에 넣고 싶으면 @ResponseBody 사용
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge
    ){
        log.info("username={} , age={}" , memberName, memberAge);
        return "ok";
    }

    @RequestMapping("/request-param-v3") // http://localhost:8080/request-param-v3?username=kim&age=10
    @ResponseBody // @Controller면서 반환타입이 String이면 view를 찾게되기 때문에, HTTP 메세지에 넣고 싶으면 @ResponseBody 사용
    public String requestParamV3(
            @RequestParam String username, // 파라미터와 변수이름이 같으면 생략 가능
            @RequestParam int age
    ){
        log.info("username={} , age={}" , username, age);
        return "ok";
    }

    @RequestMapping("/request-param-v4") // http://localhost:8080/request-param-v4?username=kim&age=10
    @ResponseBody
    public String requestParamV4(String username, int age){ //요청 파라미터의 이름과 같으면 @RequestParam도 생략 가능
        log.info("username={} , age={}" , username, age);
        return "ok";
    }

    @RequestMapping("/request-param-required")
    @ResponseBody
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age){
        log.info("username={} , age={}" , username, age);
        return "ok";
    }

    @RequestMapping("/request-param-default")
    @ResponseBody
    public String requestParamDefault(
            @RequestParam(required = true , defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age){
        log.info("username={} , age={}" , username, age);
        return "ok";
    }

    @RequestMapping("/request-param-map")
    @ResponseBody
    public String requestParamMap(@RequestParam Map<String, Object> paramMap){
        log.info("username={} , age={}" , paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }





}
