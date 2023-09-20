package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * N + 1 문제 존재
     * order 조회 1번 (order의 조회 결과수 N)
     * order -> Member 지연 로딩 조회 N번
     * order -> Delivery 지연 로딩 조회 N번
     * 만약 order의 결과가 4개라면 최악의 경우 1 + 4 + 4 번 실행된다.
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(SimpleOrderDto::new) // map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * V2는 N+1문제 발생으로 너무 많은 쿼리가 나가고 있다.
     * 해당 문제를 해결하기 위해 패치조인을 한다.
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(SimpleOrderDto::new) // map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }



    @Data

    private class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getMember().getAddress(); // lazy 초기화
        }
    }
}
