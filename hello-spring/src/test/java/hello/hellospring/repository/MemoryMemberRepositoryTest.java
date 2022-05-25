package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach // 한 메서드의 실행이 끝날 때마다 작동함
    public  void afterEach() {
        repository.clearStore();  
        // 이 코드가 없으면 에러남! Test메서드의 순서들은 보장되어있지 않음. 
        // 순서대로 수행 x . 
        // 따라서 앞에서 객체가 이미 생성되어 버리면 findByName에서 오류날 수 있음. 
        // 그러므로 비워줘야함
    }

    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get(); // 반환타입이 Optional인데, Optional인 경우 get으로 꺼낼 수 있음
        assertThat(member).isEqualTo((result));
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2);

    }
}

// Test는 서로 순서와 상관없이, 서로 의존적이지 않게 설계가 되어야 함
// 그러기 위해서는 하나의 테스트가 끝날 때마다 저장소나 , 공용 데이터들을 깔끔하게 지워줘야 문제가 없음
