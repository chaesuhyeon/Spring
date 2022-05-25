package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em; // JPA는 EntityManager라는 걸로 모든 것을 동작

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);// em.find(타입, 식별자pk값);
        return Optional.ofNullable(member); // Optional로 반환하기 때문에 값이 없을 수도 있으므로 ofNullable
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select  m from Member m where m.name = :name", Member.class) //jpql문
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
   }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select  m from Member m", Member.class)
                .getResultList();
    }
}
