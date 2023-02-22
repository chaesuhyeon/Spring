package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany// 다대다는 실무에서 쓰면 안됨!!
    @JoinTable( // 다대다를 쓸 경우에 연관관계 주인에 @JoinTable 사용해서 중간테이블을 매핑
            name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();

    // 부모 - 자식 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 메서드==//
    // db에서 값 저장은 연관관계의 주인쪽에서 해주겠지만, 양방향으로 원하는대로 값을 조회하기 위해서
    // 이렇게 값을 넣어주는 메서드를 작성해준다.
    // 연관관계 메서드의 작성 위치는 실질적으로 많이 조회하는 or 핵심적으로 컨트롤하는 곳에 적어주는게 좋다.
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
