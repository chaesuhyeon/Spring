package hello.proxy.app.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping // 스프링은 @Controller 또는 @RequestMapping이 있어야 스프링 컨트롤러로 인식한다.
@ResponseBody // RestController 처럼 쓸 것이기 때문에 해당 어노테이션 붙임
public interface OrderControllerV1 {

    // request()는 LogTrace()를 적용할 대상이고
    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    // noLog()는 LogTrace()를 적용하지 않을 대상
    @GetMapping("/v1/no-log")
    String noLog();
}
