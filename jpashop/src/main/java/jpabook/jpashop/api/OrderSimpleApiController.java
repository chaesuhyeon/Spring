package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
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
     * 엔티티를 그대로 반환
     * 사용 x
     * 무한루프 문제 발생 -> 양방향으로 연결된 곳중 한 곳에 @JsonIgnore 사용 -> ByteBuddy 문제발생 -> Hibernate5Module 사용
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember() // 여기까지 프록시 객체(fetch - lazy 이므로)
                    .getName(); // name까지 불러오면 lazy 강제 초기화
            order.getDelivery() // 여기까지 프록시 객체(fetch - lazy 이므로)
                    .getAddress();
        }
        return all;
    }


}
