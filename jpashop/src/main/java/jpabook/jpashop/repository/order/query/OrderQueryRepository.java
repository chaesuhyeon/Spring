package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 화면에 의존적인 repository
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders(); // 컬렉션(orderItem)을 제외한 order 데이터 조회
        result.forEach(o->{
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // 쿼리로 컬렉션 값을 가져옴
            o.setOrderItems(orderItems); // 먼저 조회된 result에 있는 orderItem에 나중에 조회한 컬렉션(orderItem)값을 넣어줌
        });

        return result;
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
