package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository // @Repository를 사용하면 컴포넌트 스캔해서 자동으로 스프링 빈으로 관리해주게 함
@RequiredArgsConstructor
public class MemberRepositoryOld {

//    @PersistenceContext -> 이 어노테이션 사용해도 되는데,  spring data jpa가 autowired로 사용하도록 지원해줘서 @RequiredArgsConstructor를 사용해도 된다.
    private final EntityManager em; // 스프링이 엔티티 매니저를 만들어서 자동으로 주입시켜줌

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
