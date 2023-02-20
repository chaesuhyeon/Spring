package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "order") // 연관관계의 주인이 아님. orderItem의 order
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id") // OneToOne은 아무쪽이나 연관관계 주인으로 해도 되지만, 주로 select를 많이 하는 곳을 연관관계 주인으로 하는 경우가 일반적. 이 설계에서는 배달에서 주문을 조회하는 것보다는 주문에서 배달정보를 조회하는 경우가 더 많으므로 Order의 delivery가 연관관계의 주인이 됨
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]


}
