package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository //안에 들어가면 @Component가 있어서 컴포넌트 스캔의 대상이 됨
public class ItemRepository {

    private static final Map<Long , Item> store = new HashMap<>(); //static 사용 주의
    //스프링은 싱글톤 기반이므로 멀티쓰레드 환경에서 HashMap사용하면 안됨. ConcurrentHashMap을 사용해야함
    private static long sequence = 0L; //static

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId() , item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId , Item updateParam){
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }
}
