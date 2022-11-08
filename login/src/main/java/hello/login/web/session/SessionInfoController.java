package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session == null){
            return "세션이 없습니다.";
        }

        //세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name))); // session name=loginMember, value=Member(id=1, loginId=test, name=테스터, password=test!)

        log.info("sessionId={}", session.getId()); // sessionId=E3F261F7235D327D9C7A5C0FC0D96405
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval()); // getMaxInactiveInterval=1800 // 세션 유효 시간
        log.info("creationTime={}", new Date(session.getCreationTime())); // creationTime=Tue Nov 08 08:47:59 KST 2022 // 세션 생성 일시
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime())); // 세션과 연결된 사용자가 최근에 서버에 접근한 시간. 클라이언트에서 서버로 sessionId를 요청한 경우에 갱신 됨 // lastAccessedTime=Tue Nov 08 08:47:59 KST 2022
        log.info("isNew={}", session.isNew()); // isNew=false (새로 생성x) // 새로 생성된 세션인지, 이미 과거에 만들어져서 클라이언트에서 서버로 sessionId를 요청해서 조회된 세션인지 여부

        return "세션 출력";
    }
}
