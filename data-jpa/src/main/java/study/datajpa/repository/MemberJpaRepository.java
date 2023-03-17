package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll(){
        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
        return result;
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member); // null일수도 아닐 수도 있다는 것을 명시시
   }

   public long count(){
        return em.createQuery("select count(m) from Member m" , Long.class) // count 반환타입이 long이라서 Long으로 지정정
               .getSingleResult(); // 단건 조회
   }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age" , Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    /***
     * @NamedQuery 사용
     */
    public List<Member> findByUsername(String username){

        List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
        return resultList;
    }

    /**
     * 순수 JPA 페이징
     */
    public List<Member> findByPage(int age, int offset, int limit){
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc",Member.class)
                .setParameter("age", age)
                .setFirstResult(offset) // 어디서부터 가져올 것인지
                .setMaxResults(limit) // 가져올 개수
                .getResultList();
    }

    /**
     * 순수 페이징
     * totalCount
     */
    public long totalCount(int age){
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class) // count(m)는 long으로 반환된다. 따라서 메서드 반환 타입을 long으로 지정
                .setParameter("age", age)
                .getSingleResult();
    }
}
