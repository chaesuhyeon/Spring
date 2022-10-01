package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

//    @Autowired // 생성자가 1개만 있으면 Autowired 생략 가능
    // @RequiredArgsConstructor사용하면 final붙은 애를 가지고 아래와 같은 생성자를 만들어줌
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    //item 목록 가져오기
    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    //item 상세보기
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId , Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    //상품 등록 폼 보여주기
    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //상품 등록
    @PostMapping("/add")
    public String save(){
        return "basic/addForm";
    }

    @GetMapping("/edit")
    public String editForm(){
        return "basic/editForm";
    }

    /**
     * 테스트용 데이터 추가(처음에 데이터 없으면 잘되고 있는지 모르므로..)
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));

    }
}
