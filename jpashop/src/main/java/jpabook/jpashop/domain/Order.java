package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orders") // order가 예약어라서 orders로 명칭 변경
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id") // 매핑은 뭘로 할것인지 지정. 외래키 이름이 member_id가 됨
    private Member member; // order와 member는 manyToOne 관계 , order가 외래키를 갖고 있으므로 연관관계 주인
}
