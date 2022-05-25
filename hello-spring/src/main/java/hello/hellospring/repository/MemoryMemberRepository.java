package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;


public class MemoryMemberRepository implements  MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // null이 반환될 가능성이 있으면 Optional로 감싸줌
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream() // 루프 돌리며 찾음
                .filter(member -> member.getName().equals(name)) // member.getNmae이 파라미터로 넘어온 name과 같은지 필터링
                .findAny(); // name이 같은 경우에만 필터링이 되며, 그 값을 반환
// Map에서 돌면서 찾으면 그 값을 반환하고 , 끝까지 돌렸는데도 없으면 Optional에 null이 포함돼서 반환
    }

    @Override
    public List<Member> findAll() { // 위는 Map인데 반환은 List임
        return new ArrayList<>(store.values()); // store에 있는 Member반환
    }

    public void clearStore(){
        store.clear();
    }
}

// 위에서 작성한 코드들이 잘 작성됐는지 확인하기 위해서 test code를 작성해야 함