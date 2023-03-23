package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    /**
     * 테스트 전에 데이터 넣어둠
     */
    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em); // 이부분 주의
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL(){
        // member1을 찾아라
        String qlString = "select m from Member m where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * Q-Type 활용
     */
    @Test
    public void startQuerydsl(){
//        QMember m = new QMember("m"); // m은 별칭
//        QMember m = QMember.member; // 기본 인스턴스 사용 -- > static import 가능

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1")) // 파라미터 바인딩 처리
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * 검색 조건 쿼리
     */
    @Test
    public void search() {
        Member findMember = queryFactory
                .selectFrom(member) // select member from member 라서 합쳐버림
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * 검색 조건 쿼리
     * .and 사용 대신에 ,(쉼표)로 조건 이을 수 있음
     * ,를 사용하면 null이 있으면 null을 무시한다.(동적 쿼리 짤 때 유용)
     */
    @Test
    public void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member) // select member from member 라서 합쳐버림
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * 결과 조회
     */
    @Test
    public void resultFetch(){

        // fetch
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch(); // member의 목록을 List로 반환

        // fetchOne
        Member fetchOne = queryFactory
                .selectFrom(QMember.member)
                .fetchOne(); // 단건 조회

        // fetchFirst
        Member fetchFirst = queryFactory
                .selectFrom(QMember.member)
                .fetchFirst();// limit(1).fetchOne() 하고 똑같음

        // fetchResults
        QueryResults<Member> fetchResults = queryFactory
                .selectFrom(member)
                .fetchResults(); // 페이징 정보 포함
        fetchResults.getTotal();
        List<Member> content = fetchResults.getResults();

        // fetchCount
        long fetchCount = queryFactory
                .selectFrom(member)
                .fetchCount();

    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    public void sort(){
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging1(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();
        assertThat(result.size()).isEqualTo(2);  //limit을 2로 했으니 size가 2가 나와야 함
    }

    @Test
    public void paging2(){
        QueryResults<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults(); // total Count가 필요하면 fetchResults 사용

        assertThat(result.getTotal()).isEqualTo(4);
        assertThat(result.getLimit()).isEqualTo(2);
        assertThat(result.getOffset()).isEqualTo(1);
        assertThat(result.getResults().size()).isEqualTo(2);
    }

    /**
     * 집합
     * Tuple로 반환됨
     */
    @Test
    public void aggregation(){
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    /**
     * group by
     * 팀의 이름과 각 팀의 평균 연령을 구해라
     */
    @Test
    public void group(){
        List<Tuple> result = queryFactory
                .select(
                        team.name,
                        member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15); // (10+20)/2

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35); // (30+40)/2
    }

    /**
     * 기본 조인
     */
    @Test
    public void join(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team) // team은 QTeam.team  , join 말고 leftJoin, rightJoin도 가능
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    /**
     * 세타 조인
     * 연관관계가 없어도 join 가능
     * 회원의 이름이 팀 이름과 같은 회원 조회(말도 안되는 예제지만 한번 해봄)
     */
    @Test
    public void theta_join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC")); // team name이 teamC는 없기 때문에 이름이 teamC인 회원은 join 결과에 나오지 않음

        List<Member> result = queryFactory
                .select(member)
                .from(member, team) // 두개 그냥 나열
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * join - on절 예제
     * ex) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조회, 회원은 모두 조회
     * JPQL : select m,t from Member m left join m.team t on t.name = 'teamA'
     */
    @Test
    public void join_on_filtering(){
        //외부 조인
       List<Tuple> result1 = queryFactory // 반환 타입이 Tuple로 나온 이유는 select 할 때 여러가지 타입으로 나왔기 때문
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();

        // 내부조인 - on절사용
        List<Tuple> result2 = queryFactory // 반환 타입이 Tuple로 나온 이유는 select 할 때 여러가지 타입으로 나왔기 때문
                .select(member, team)
                .from(member)
                .join(member.team, team).on(team.name.eq("teamA"))
                .fetch();

        // 내부조인 - where절사용(내부조인-on 결과와 똑같음)
        List<Tuple> result3 = queryFactory // 반환 타입이 Tuple로 나온 이유는 select 할 때 여러가지 타입으로 나왔기 때문
                .select(member, team)
                .from(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result1) {
            System.out.println("tuple = " + tuple);
        }

/*        tuple = [Member(id=3, username=member1, age=10), Team(id=1, name=teamA)]
        tuple = [Member(id=4, username=member2, age=20), Team(id=1, name=teamA)]
        tuple = [Member(id=5, username=member3, age=30), null] // on절에서 teamA인 회원만 가져오도록 했기 때문에 teamB는 null로 나옴(left join이라서.. 그냥 join으로 하면 member1,2만 나옴)
        tuple = [Member(id=6, username=member4, age=40), null] */
    }

    /**
     * 연관관계 없는 엔티티 외부 조인
     * 회원의 이름이 팀 이름과 같은 회원 조회(말도 안되는 예제지만 한번 해봄)
     */
    @Test
    public void join_on_no_relation(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC")); // team name이 teamC는 없기 때문에 이름이 teamC인 회원은 join 결과에 나오지 않음

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                // join문 문법 다름!! 원래는 join(member.team, team) 으로 해야함 ,
                // 그리고 연관관계 없는 엔티티 조인할 때 세타조인도 사용할 수 있었는데, 세타조인은 외부 조인 안됐음 -> on으로 해결
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

/*        tuple = [Member(id=3, username=member1, age=10), null]
        tuple = [Member(id=4, username=member2, age=20), null]
        tuple = [Member(id=5, username=member3, age=30), null]
        tuple = [Member(id=6, username=member4, age=40), null]
        tuple = [Member(id=7, username=teamA, age=0), Team(id=1, name=teamA)]
        tuple = [Member(id=8, username=teamB, age=0), Team(id=2, name=teamB)]
        tuple = [Member(id=9, username=teamC, age=0), null]*/
    }

    /**
     * 페치 조인 미적용
     */
    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo(){
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 지연로딩 테스트 하기 위해 사용(Team은 불러와지면 안됨)
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    /**
     * 페치 조인 적용
     */
/*    @PersistenceUnit
    EntityManagerFactory emf;*/
    @Test
    public void fetchJoinUse(){
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin() // fetchJoin 사용
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 지연로딩 테스트 하기 위해 사용(Team은 불러와지면 안됨)
        assertThat(loaded).as("페치 조인 미적용").isTrue();
    }

    /**
     * 서브 쿼리 eq 사용
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQuery_eq(){

        QMember memberSub = new QMember("memberSub"); // alias가 겹친다면 Q-Type을 새롭게 생성해줘야함

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max()) // 40살이 최대
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(40);
    }

    /**
     * 서브 쿼리 goe 사용
     * 나이가 평균 이상인 회원
     */
    @Test
    public void subQuery_goe(){

        QMember memberSub = new QMember("memberSub"); // alias가 겹친다면 Q-Type을 새롭게 생성해줘야함

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())  // 평균 : 25
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(30,40);
    }

    /**
     * 서브 쿼리 in 사용
     */
    @Test
    public void subQuery_in(){

        QMember memberSub = new QMember("memberSub"); // alias가 겹친다면 Q-Type을 새롭게 생성해줘야함

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10)) // 20,30,40
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(20,30,40);
    }

    /**
     * select 절에 subquery
     */
    @Test
    public void selectSubQuery(){
        QMember memberSub = new QMember("memberSub"); // alias가 겹친다면 Q-Type을 새롭게 생성해줘야함
        List<Tuple> result = queryFactory
                .select(
                        member.username,
                        select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

/*        tuple = [member1, 25.0]
        tuple = [member2, 25.0]
        tuple = [member3, 25.0]
        tuple = [member4, 25.0]*/
     }

    /**
     * case 문
     */
    @Test
     public void basicCase(){
        List<String> result = queryFactory
                .select(
                        member.age
                                .when(10).then("열살")
                                .when(20).then("스무살")
                                .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

/*      s = 열살
        s = 스무살
        s = 기타 // age 30
        s = 기타 // age 40
*/
    }

    /**
     * 복잡한 Case문
     */
    @Test
    public void complexCase(){
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

  /*      s = 0~20살
        s = 0~20살
        s = 21~30살
        s = 기타*/
    }

    /**
     * 상수 constant
     */
    @Test
    public void constant(){
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

/*      tuple = [member1, A]
        tuple = [member2, A]
        tuple = [member3, A]
        tuple = [member4, A]
*/
    }

    /**
     * 문자 더하기 concat
     */
    @Test
    public void concat(){
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue())) // age는 type이 int라서 string으로 변환
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
        // s = member1_10 -> 이름 member 1 + "_" + 나이 10
    }

    /**
     * 프로젝션 대상이 하나일 때
     */
    @Test
    public void simpleProjection(){
        List<String> reuslt = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : reuslt) {
            System.out.println("s = " + s);
        }

/*
        s = member1
        s = member2
        s = member3
        s = member4
*/
    }

    /**
     * 프로젝션 대상이 둘 이상일 때
     * 튜플로 조회
     */
    @Test
    public void tupleProjection(){
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
/*
        username = member1
        age = 10
        username = member2
        age = 20
        username = member3
        age = 30
        username = member4
        age = 40
 */
    }

    /**
     * DTO로 반환 JPQL 사용
     */
    @Test
    public void findDtoByJPQL(){
        List<MemberDto> result = em.createQuery(
                        "select " +
                                "new study.querydsl.dto.MemberDto(m.username, m.age) " +
                                "from Member m", MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
/*
        memberDto = MemberDto(username=member1, age=10)
        memberDto = MemberDto(username=member2, age=20)
        memberDto = MemberDto(username=member3, age=30)
        memberDto = MemberDto(username=member4, age=40)
 */
    }

    /**
     * DTO로 반환
     * 프로퍼티 접근 - Setter
     */
    @Test
    public void findDtoBySetter(){
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

/*        memberDto = MemberDto(username=member1, age=10)
        memberDto = MemberDto(username=member2, age=20)
        memberDto = MemberDto(username=member3, age=30)
        memberDto = MemberDto(username=member4, age=40)*/
    }

    /**
     * DTO로 반환
     * 필드 직접 접근
     */
    @Test
    public void findDtoByField(){
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

/*        memberDto = MemberDto(username=member1, age=10)
        memberDto = MemberDto(username=member2, age=20)
        memberDto = MemberDto(username=member3, age=30)
        memberDto = MemberDto(username=member4, age=40)*/
    }

    /**
     * DTO로 반환
     * 생성자 사용
     */
    @Test
    public void findDtoByConstructor(){
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

/*        memberDto = MemberDto(username=member1, age=10)
        memberDto = MemberDto(username=member2, age=20)
        memberDto = MemberDto(username=member3, age=30)
        memberDto = MemberDto(username=member4, age=40)*/
    }

    /**
     * DTO로 반환
     * 별칭이 다를 때
     */
    @Test
    public void findUserDto(){
        QMember memberSub = new QMember("memberSub"); // alias가 겹친다면 Q-Type을 새롭게 생성해줘야함

        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"), // 별칭 지정을 안해주면 member의 username과 userDto의 name이 매칭이 안돼서 null로 나옴
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(member.age.max())
                                        .from(memberSub), "age")
                ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
/*
        userDto = UserDto(name=member1, age=40)
        userDto = UserDto(name=member2, age=40)
        userDto = UserDto(name=member3, age=40)
        userDto = UserDto(name=member4, age=40)
 */
    }

    /**
     * @QueryProjection
     */
    @Test
    public void findDtoByQueryProjection(){
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
/*
        memberDto = MemberDto(username=member1, age=10)
        memberDto = MemberDto(username=member2, age=20)
        memberDto = MemberDto(username=member3, age=30)
        memberDto = MemberDto(username=member4, age=40)
 */

    }

    /**
     * 동적 쿼리
     * BooleanBuilder
     */
    @Test
    public void dynamicQuery_BooleanBuilder() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder(member.username.eq(usernameCond)); // 초기값 넣어줄 수도 있음(null이 아닌 가정 하에)
//        BooleanBuilder builder = new BooleanBuilder();
        if (usernameCond != null) { // null이 아니면 조건에 추가
            builder.and(member.username.eq(usernameCond));
        }
        if (ageCond != null) { // null이 아니면 조건에 추가
            builder.and(member.age.eq(ageCond));
        }
        return queryFactory
                .selectFrom(member)
                .where(builder) // 조건 넣어줌
                .fetch();
    }

    /**
     * 동적 쿼리
     * Where 다중 파라미터
     */
    @Test
    public void dynamicQuery_WhereParam() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                .where(allEq(usernameCond, ageCond))
                .fetch();
    }


    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond == null ? null : member.username.eq(usernameCond); // usernameCond가 null이면 null을 반환. 그렇지 않으면 조건 반환
    }
    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond == null ? null : member.age.eq(ageCond); // usernameCond가 null이면 null을 반환. 그렇지 않으면 조건 반환

    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond)); // usernameCond가 null이면 null을 반환. 그렇지 않으면 조건 반환

    }

    /**
     * 벌크 연산 - 수정
     */
    @Test
    public void bulkUpdate(){

        // member1 = 10 -> 비회원
        // member2 = 20 -> 비회원
        // member3 = 30 -> member3
        // member4 = 40 -> member4

        // count : 영향을 받는 row 수가 나옴
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        em.flush();
        em.clear(); // 영속성 컨텍스트 초기화

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();
    }

    /**
     * 벌크 연산 - 더하기
     */
    @Test
    public void bulkAdd(){
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();
    }

    /**
     * 벌크 연산 - 곱하기
     */
    @Test
    public void bulkMultiply(){
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.multiply(1))
                .execute();
    }

    /**
     * 벌크 연산 - 삭제
     */
    @Test
    public void bulkDelete(){
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18)) // 18살 이상의 회원을 지움
                .execute();
    }

    /**
     * SQL function
     * replace
     */
    @Test
    public void sqlFunction_replace(){
        List<String> result = queryFactory
                .select(Expressions.stringTemplate(
                        "function('replace', {0},{1},{2})",
                        member.username, "member", "M")) // username에서 member를 M으로 replace
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

/*      s = M1
        s = M2
        s = M3
        s = M4
*/
    }

    /**
     * SQL function
     * lower 소문자 변경
     */
    @Test
    public void sqlFunction_lower(){
        List<Member> result = queryFactory
                .select(member) // username에서 member를 M으로 replace
                .from(member)
                .where(member.username.eq(member.username.lower())) // 일반적으로 db에서 다 제공하는 기능들은 querydsl에서 구현되어 있음(안시 표준)
//                .where(member.username.eq(
//                        Expressions.stringTemplate(
//                                "function('lower', {0})",
//                                member.username)))
                .fetch();

        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }
    }

}

