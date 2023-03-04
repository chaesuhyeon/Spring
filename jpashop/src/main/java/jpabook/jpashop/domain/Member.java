package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.print.attribute.standard.MediaSize;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter // setter는 필요할 때만 사용하는 것을 추천
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long Id;

    @NotEmpty
    private String name;

    @Embedded //Address 클래스에서는 @Embeddable 사용(두 곳중에 한곳에만 어노테이션 사용 가능하지만 두 곳다 적어줌)
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member") // mappedBy는 연관관계 주인이 아닌곳에 작성 (읽기 전용이라는 뜻). Order 테이블에 있는 member를 지정
    private List<Order> orders = new ArrayList<>(); // member의 입장에서 하나의 회원이 여러개의 상품을 주문하기 때문에 oneToMany
}
