package helllo.typeconverter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {
    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); // 문자 타입 조회
        Integer intValue = Integer.valueOf(data);
        System.out.println("intValue = " + intValue);
        return "ok";
    }

    /**
     * http://localhost:8080/hello-v2?data=10
     * 위와 같이 요청해도 10은 숫자가 아니라 "문자"이다.
     * 스프링이 중간에서 문자 -> 숫자로 변환을 해줬기 때문에 @RequestParam을 사용하면 숫자로 받을 수 있다.
     */
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        System.out.println("data = " + data);
        return "ok";
    }
}
