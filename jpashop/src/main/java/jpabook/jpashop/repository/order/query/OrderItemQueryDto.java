package jpabook.jpashop.repository.order.query;

import lombok.Data;

/**
 * 화면을 위한 Dto
 * query 패키지를 만들어서 화면단 계층에 만들어줌
 */
@Data
public class OrderItemQueryDto {
    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
