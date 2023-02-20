package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

}
