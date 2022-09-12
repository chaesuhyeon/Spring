package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AutomicLong 사용을 고려
 */
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // static이므로 new로 생성해도 1개만 생성됨
    private static long sequence = 0L; // static이므로 new로 생성해도 1개만 생성됨

    private static final MemberRepository instance = new MemberRepository();
    
    public static MemberRepository getInstance(){
        return instance;
    }
    
    private MemberRepository(){
        // 싱글톤으로 만들기 위해서 생성자를 private으로 막음
    }

    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }

}
