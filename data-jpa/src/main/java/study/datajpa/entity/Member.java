package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 만들어줘야함
@ToString(of = {"id", "username", "age"}) // 객체 바로 찍어도 출력되게 , 연관관계 필드 (ex.team)은 toString에 넣게 되면 서로 참조하게 돼서 무한루프 돌 수 있으니 넣지 말아야함
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username")
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id") // team_id 는 fk , member가 연관관계의 주인 (외래키를 가지고 있기 때문에)
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;

        if(team != null){
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    /**
     * setter 안쓰려면 이런 메소드 필요
     */
    public void changeUsername(String username){
        this.username = username;
    }

    //==연관관계 메서드==//
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
