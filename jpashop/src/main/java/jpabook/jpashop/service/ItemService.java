package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;


    @Transactional // readOnly면 저장이 안되기 때문에 따로 설정해줌
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    // @Transactional을 따로 설정하지 않았기 때문에 맨위에 선언했던 @Transactional(readOnly = true) 적용
    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
