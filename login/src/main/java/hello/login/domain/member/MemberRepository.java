package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();

    private static long sequence = 0L; // static 사용

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}" , member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    // 아이디 찾기
    public Optional<Member> findByLoginId(String loginId){

/*        List<Member> all = findAll();
        for (Member m : all) {
            if (m.getLoginId().equals(loginId)){ //파라미터로 들어온 loginId와 같은지 비교
                return Optional.of(m);
            }
        }
        return Optional.empty();*/

        // 위에 코드를 줄이면 아래코드
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId)) // 조건에 만족하는 데이터들만 다음으로 넘어감(만족하지 않으면 버려짐)
                .findFirst(); // 조건을 만족해서 넘어온 데이터들 중에 먼저 나온 것이 반환됨
    }

    public List<Member> findAll(){

        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }

}
