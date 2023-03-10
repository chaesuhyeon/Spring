package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepositoryOld memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 예제를 간단히 하기 위해 회원에 있는 주소 정보를 배송지로 설정

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // OrderItem orderItem = new OrderItem(); // OrderItem 생성 메소드를 createOrderItem으로만 하고 new OrderItem으로 생성하지 못하도록 막기 위해 protected + 생성자 사용

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        //원래는 Delivery도 Repository에 save 로직을 만들어서 persist 해주고,
        //OrderItem역시 그래야하지만, Order > orderItem에 있는  cascade 옵션 때문에 order만 persist 해주면 orderItem도 persist된다 (Delivery도 같은 원리)
        //OrderItem도 Order에서만 사용하고, Delivery도 Order에서만 사용한다. 이럴 경우와 라이프 사이클이 똑같은 경우에는 cascade를 사용할 수 있다.
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order  = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
