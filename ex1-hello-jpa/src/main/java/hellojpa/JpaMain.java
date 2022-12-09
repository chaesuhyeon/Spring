package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        // 이 부분에서 실제 동작하는 코드를 작성
        EntityTransaction tx = em.getTransaction(); // 트랜잭션
        tx.begin();// 트랜잭션 시작ㄴ

        try{
            // 회원 등록
            /*
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");
            em.persist(member);
            tx.commit();// 데이터 커밋
            */

            // 회원 조회
            /*
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());
            tx.commit();
            */

            // 회원 조회(조건 O. JPQL)
            List<Member> result = em.createQuery("select m from Member as m", Member.class).getResultList();
            for (Member member : result) {
                System.out.println("member.name = " + member.getName());
            }
            tx.commit();
            // 회원 삭제
            /* Member findMember = em.find(Member.class, 1L);
            em.remove(findMember);
            tx.commit();
            */

            // 회원 수정
            /* Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");
            tx.commit();
            */
        } catch(Exception e){
            tx.rollback(); // 예외가 발생하면 rollback
        } finally {
            em.close();
        }
        
        emf.close();
    }

}
