package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){ // id값이 없다는 것은 완전 새롭게 생성한 객체라는 뜻
            em.persist(item); // 신규로 등룩
        } else { // id값이 있다는 것은 db에 있는 값을 가져옴
            em.merge(item); // update 개념 (엄밀히 말하면 update와는 다르지만 비슷한것으로 이해)
        }
    }


    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
