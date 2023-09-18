package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    /**
     * 엔티티를 그대로 반환하는 방법 (지양해야할 방법)
     * 엔티티를 그대로 노출할 경우 수정사항이 발생했을 때 api 스펙 자체가 변경되는 문제점이 있다. (장애로 이어질 가능성 높음)
     * 따라서 이 방법은 지양하고 DTO를 반환하는 방법을 지향해야한다.
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화 (getMember 까지는 프록시. 실제 name을 조회할 때 쿼리가 나가고 실제 객체가 조회된다.)
            order.getDelivery().getAddress();
        }
        return all;
    }
}
