package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV2", urlPatterns ="/front-controller/v2/*" )
public class FrontControllerServletV2 extends HttpServlet {

    // 매핑정보
    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();// uri를 받을 수 있음 (ex) "/front-controller/v1/members/"
        ControllerV2 controller = controllerMap.get(requestURI); // 받은 uri 가지고 map에서 컨트롤러를 꺼냄(key, value) , 다형성에 의해서 부모타입인 Controller 인터페이스로 받을 수 있음

        //컨트롤러가 없는 경우 예외 처리
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 컨트롤러가 잘 조회될 경우 @Overried된 메서드 호출
        MyView view = controller.process(request, response);
        view.render(request,response);

    }
}
