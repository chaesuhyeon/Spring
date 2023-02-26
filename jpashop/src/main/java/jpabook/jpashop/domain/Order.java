package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order가 예약어라서 orders로 명칭 변경
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 접근제어자 protected 기본생성자
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 매핑은 뭘로 할것인지 지정. 외래키 이름이 member_id가 됨
    private Member member; // order와 member는 manyToOne 관계 , order가 외래키를 갖고 있으므로 연관관계 주인

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL) // 연관관계의 주인이 아님. orderItem의 order
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // OneToOne은 아무쪽이나 연관관계 주인으로 해도 되지만, 주로 select를 많이 하는 곳을 연관관계 주인으로 하는 경우가 일반적. 이 설계에서는 배달에서 주문을 조회하는 것보다는 주문에서 배달정보를 조회하는 경우가 더 많으므로 Order의 delivery가 연관관계의 주인이 됨
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]


    //==연관관계 메서드==//
    // db에서 값 저장은 연관관계의 주인쪽에서 해주겠지만, 양방향으로 원하는대로 값을 조회하기 위해서
    // 이렇게 값을 넣어주는 메서드를 작성해준다.
    // 연관관계 메서드의 작성 위치는 실질적으로 많이 조회하는 or 핵심적으로 컨트롤하는 곳에 적어주는게 좋다.
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this); // 회원이 뭘 주문했는지 넣어준다.
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    //==생성 메서드==//
    public static Order createOrder(Member member , Delivery delivery, OrderItem... orderItems){ // OrderItem은 여러개 가능(여러개 넘길 수 있음)
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // 주문 상태
        order.setOrderDate(LocalDateTime.now()); // 주문 시간
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     * 재고 수량을 변경해줘야 함
     * 배송이 끝나면 주문 취소 불가
     */
    public void cancel() {

        //배송이 끝나면 주문 취소 불가
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // 상품에도 취소를 해줘야함
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum(); // 0으로 초기화
       /*
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        */
        return totalPrice;
    }



}
