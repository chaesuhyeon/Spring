package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 화면에 의존적인 repository
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    /**
     * OrderItem을 가져오기 위해 loop를 돈다.(N+1)문제의 원인
     */
    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders(); // 컬렉션(orderItem)을 제외한 order 데이터 조회
        result.forEach(o->{
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // 쿼리로 컬렉션 값을 가져옴
            o.setOrderItems(orderItems); // 먼저 조회된 result에 있는 orderItem에 나중에 조회한 컬렉션(orderItem)값을 넣어줌
        });

        return result;
    }

    /**
     *  findOrderQueryDtos()의 loop 문제 해결
     *  where절에 in절 사용
     *  쿼리를 1번 날리고, 메모리에서 map에 넣어준 다음에 메모리에서 forEach로 돌면서 매칭을 시켜 값을 셋팅해준다.
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        // orderId값을 list에 넣는다.
        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());

        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" + // dto 생성자에 있는 파라미터 넣어줌
                                " from OrderItem oi" +
                                " join oi.item i" + // OrderItem과 Item은 xToOne 관계라서 join 해줬음
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class) // in절 사용
                .setParameter("orderIds", orderIds)
                .getResultList();

        // orderItem을 map으로 변경(최적화)
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

        result.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address,i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i" , OrderFlatDto.class)
                .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" + // dto 생성자에 있는 파라미터 넣어줌
                        " from OrderItem oi" +
                        " join oi.item i" + // OrderItem과 Item은 xToOne 관계라서 join 해줬음
                        " where oi.order.id = :orderId" , OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
}
