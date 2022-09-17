package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV4", urlPatterns ="/front-controller/v4/*" )
public class FrontControllerServletV4 extends HttpServlet {

    // 매핑정보
    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();// uri를 받을 수 있음 (ex) "/front-controller/v3/members/"
        ControllerV4 controller = controllerMap.get(requestURI); // 받은 uri 가지고 map에서 컨트롤러를 꺼냄(key, value) , 다형성에 의해서 부모타입인 Controller 인터페이스로 받을 수 있음

        //컨트롤러가 없는 경우 예외 처리
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        Map<String , Object> model = new HashMap<>(); // paraMap 뿐만 아니라 model도 넘겨줘야 함
        String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);
        view.render(model, request, response);



    }

    private MyView viewResolver(String viewName) {
        MyView view = new MyView("/WEB-INF/views/" + viewName + ".jsp");
        return view;
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String,String> paramMap = new HashMap<>();

        // paramMap
        request.getParameterNames().asIterator() // 모든 파라미터 다 꺼내서 이터레이터로 바꿈
                .forEachRemaining(paramName-> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
