package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data라는 어노테이션도 있는데 핵심 도메인에서는 사용하는 것이 위험하므로 Getter와 Setter정도만 사용할 것. 멋대로 잘못 동작할 수 있으므로
//단순히 왔다갔다할 때 쓰는 dto에는 @Data사용해도 됨
//@Getter
//@Setter
@Data // 단순 예제이므로 Data사용
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    // 기본생성자
    public Item() {
    }

    // id를 제외한 생성자
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
