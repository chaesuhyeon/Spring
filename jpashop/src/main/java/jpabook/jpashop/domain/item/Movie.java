package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@DiscriminatorValue("M") // single table(상속관계) 전략이므로 데이터를 저장할때 어떤 테이블인지 구분할 수 있어야 하므로 @DiscriminatorValue 사용
public class Movie extends Item{ // Item 상속 받음
    private String director;
    private String actor;

}
