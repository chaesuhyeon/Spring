package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 매핑이기 때문에 상속관계 전략을 부모클래스에 지정해야한다.
@DiscriminatorColumn(name = "dtype") // single table 전략일 때 구분할 수 있는 필드 지정. Book, Album, Movie 에는 @DiscriminatorValue사용하여 구분 필드 이름 지정
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items") // 다대다는 실무에서 쓰면 안됨!!
    private List<Category> categories = new ArrayList<>();


    //==비즈니스 로직==//

    // 많이들 service에서 관련 필드를 가져와서 로직을 구현하고 setter를 이용해서 값을 셋팅하는 방식을 사용하지만
    // 원래는 객체지향적으로 하려면 data를 가지고 있는 쪽에 비즈니스 로직을 넣는게 좋다.
    // 여기서는 재고 수량(stockQuantity를)에 대한 비즈니스 로직을 만들어줬는데 그 이유는 Item class에 stockQuantity를 정의했기 때문이다.
    /**
     * stock 증가
     */
    public void addStock(int quantity){ // 재고 수량 증가 시키는 로직
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){ // 수량이 0 이하가 될 수가 없음
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock; // 재고 수량을 남은 수량으로 다시 셋팅
    }

    //== end 비즈니스 로직==//


}
