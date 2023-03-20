package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    /**
     * 새로운 객체인지 판단하기 위한 테스트
     */
    @Test
    public void save(){
        Item item = new Item("A");
        itemRepository.save(item);
    }

}