package hello.servlet.web.frontcontroller.v1;

import hello.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberListControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberSaveControllerV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV1", urlPatterns ="/front-controller/v1/*" )
public class FrontControllerServletV1 extends HttpServlet {

    // 매핑정보
    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        String requestURI = request.getRequestURI();// uri를 받을 수 있음 (ex) "/front-controller/v1/members/"
        ControllerV1 controller = controllerMap.get(requestURI); // 받은 uri 가지고 map에서 컨트롤러를 꺼냄(key, value) , 다형성에 의해서 부모타입인 Controller 인터페이스로 받을 수 있음

        //컨트롤러가 없는 경우 예외 처리
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 컨트롤러가 잘 조회될 경우 @Overried된 메서드 호출
        controller.process(request, response);

    }
}
