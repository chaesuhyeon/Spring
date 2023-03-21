package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> , MemberRepositoryCustom{
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * namedQuery
     * 어노테이션 생략 가능
     * 어노테이션이 없어도 먼저 정의된 namedQuery를 찾고, 정외된 namedQuery가 없으면 쿼리메서드로 인식
     */
    //@Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    /**
     * 일반 값 조회
     */
    @Query("select m.username from Member  m")
    List<String> findUsernameList();

    /**
     * DTO 조회
     * new 연산자 필요
     */
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    /**
     * 컬렉션 파라미터
     */
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    /**
     * 반환 타입 컬렉션
     */
    List<Member> findListByUsername(String username);

    /**
     * 반환타입 엔티티
     * 단건
     */
    Member findMemberByUsername(String username);

    /**
     * 반환타입 Optional
     * 단건
     */
    Optional<Member> findOptionalByUsername(String username);

    /**
     * 페이징
     * 쿼리메서드
     * total count 쿼리 날려줌
     */
//    Page<Member> findByAge(int age , Pageable pageable);

    /**
     * 페이징
     * 쿼리메서드
     * count 쿼리 분리
     */
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age , Pageable pageable);


    /**
     * 페이징 slice 사용
     * total count 쿼리 x
     */
    //Slice<Member> findByAge(int age , Pageable pageable);

    /**
     * 벌크 쿼리
     * jpa는 영속성 컨텍스트로 관리하는데 벌크 연산은 이를 무시하고 db에 반영한다 -> 영속성 컨텍스트에는 반영이 안되어 조회하게되면 db에 있는 데이터와 안맞을 수도 있다.
     * @Modifying(clearAutomatically = true)을 해주면 em.clear() 자동으로 해줌
     * 벌크 연산 후에는 db와의 동기화를 위해 해줘야함
     */
    @Modifying(clearAutomatically = true) // modifying이 있어야 excutedUpdate() 해줌 <-> 없으면 getResultList 또는 getSingleResult 실행
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /**
     * @EntityGraph 이해 돕기 위한 패치 조인
     */
    @Query("select m from Member m left  join fetch m.team") // member를 조회하면 연관된 team을 같이 한방 쿼리로 가져옴
    List<Member> findMemberFetchJoin();

    /**
     * @EntityGraph 패치 조인
     */
    @Override
    @EntityGraph(attributePaths = {"team"} )
    List<Member> findAll();

    /**
     * 쿼리를 짰는데 패치조인만 추가하고 싶을 때
     */
    @EntityGraph(attributePaths = {"team"} )
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    /**
     * 쿼리 메서드 + EntityGraph
     */
    @EntityGraph(attributePaths = {"team"} )
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    /**
     * JPA Hint
     */
    @QueryHints( value = @QueryHint(name="org.hibernate.readOnly" , value = "true"))
    Member findReadOnlyByUsername(String username);

    /**
     * JPA Lock
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);
}
