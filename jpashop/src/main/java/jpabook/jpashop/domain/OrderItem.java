package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 접근제어자 protected 기본생성자
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // 외래키를 관리하므로 연관관계의 주인
    private Order order;

    private int orderPrice; // 주문 가격

    private int count; // 주문 수량

    /*
    // OrderItem 생성 메소드를 createOrderItem으로만 하고 new OrderItem으로 생성하지 못하도록 막기 위해 사용
    protected OrderItem() { // @NoArgsConstructor(access = AccessLevel.PROTECTED)와 같음
    }
    */

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // 아이템을 주문하면 기본적으로 주문 수량 만큼 재고 수량을 감소시켜야 함
        return orderItem;
    }

    //==비즈니스 로직==//

    public void cancel() {
        getItem().addStock(count); // item(OrderItem에 있는 Item)을 가져와서 재고 수량을 주문 수량만큼 늘려줌
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }


}
