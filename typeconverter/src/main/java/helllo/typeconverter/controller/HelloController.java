package helllo.typeconverter.controller;

import helllo.typeconverter.type.IpPort;
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

        /**
         * http://localhost:8080/hello-v2?data=10 이렇게 요청했을 때 숫자 10이 아니라 문자 10이 날라간다.
         *
         * 로그를 보니
         * 2023-09-02 22:17:26.981  INFO 82162 --- [nio-8080-exec-3] h.t.converter.StringToIntegerConverter   : convert source=10
         * StringToIntegerConverter가 잘 동작한 것을 확인할 수 있다.
         *
         * spring mvc가 integer data를 만들 때 컨트롤러 호출 되기 전에  StringToIntegerConverter를 호출해서 interger 10 으로 변환 뒤에 컨트롤러를 호출하고 그 변환된 값을 넘겨준다.
         */
    }

    /**
     * 직접 만든 IpPort 타입 적용
     * http://localhost:8080/ip-port?ipPort=127.0.0.1:8080
     * ipPort=127.0.0.1:8080 이렇게 문자가 넘어 간다.
     * 문자가 넘어간것을 IpPort 타입으로 변환이 잘 되어야 한다.
     *
     * [로그]
     * 2023-09-02 22:26:26.102  INFO 82296 --- [nio-8080-exec-2] h.t.converter.StringToIpPortConverter    : convert sour=127.0.0.1:8080
     * WebConfig에 추가한 StringToIpPortConverter가 잘 적용된 것을 확인할 수 있음
     */
    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        System.out.println("ipPort Ip = " + ipPort.getIp());
        System.out.println("ipPort Port = " + ipPort.getPort());

        return "ok";
    }
}
