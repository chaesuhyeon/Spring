package hello.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "helloServlet" , urlPatterns = "/hello") //  name : 서블릿 이름 . urlPattern : URL 매핑 // '/hello'라고 요청이 들어오면 실행
public class HelloServlet extends HttpServlet { // HttpServlet 상속

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
        System.out.println("request = " + request); // request = org.apache.catalina.connector.RequestFacade@4b93c41
        System.out.println("response = " + response); // response = org.apache.catalina.connector.ResponseFacade@5dc0c6f5

        String username = request.getParameter("username"); // http://localhost:8080/hello?username=kim 라고 쿼리 파라미터 있을 때 쉽게 파라미터 값 받을 수 있음
        System.out.println("username = " + username); // username = kim

        response.setContentType("text/plain"); // 헤더 정보에 들어감
        response.setCharacterEncoding("utf-8"); // 헤더 정보에 들어감
        response.getWriter().write("hello " + username); // write 하면 HTTP 메세지 바디에 들어감
    }
}
