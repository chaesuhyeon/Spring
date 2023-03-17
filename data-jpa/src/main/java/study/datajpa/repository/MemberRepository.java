package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
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

}
