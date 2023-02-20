package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // single table(상속관계) 전략이므로 데이터를 저장할때 어떤 테이블인지 구분할 수 있어야 하므로 @DiscriminatorValue 사용
public class Book extends Item{ // Item 상속 받음

    private String author;
    private String isbn;
}
