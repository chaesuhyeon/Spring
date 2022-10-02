package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 상품 등록 @RequestParam사용
     * @param itemName
     * @param price
     * @param quantity
     * @param model
     * @return
     */
//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){  // 파라미터의 이름은 addForm에서 input태그에 있는 name에 적혀진 것으로 넘어옴

        Item item = new Item();

        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item"; // 상품 저장하면 상품 상세페이지에서 보여주려고
    }


    /**
     * 상품 등록 @ModelAttribute 사용
     * @ModelAttribute는 Item 객체를 생성하고 요청 파라미터의 값을 프로퍼티 접근법 setXxx으로 입력해줌
     * @ModelAttribute는 Model에 @ModelAttribute로 지정한 객체를 자동으로 넣어준다. model.addAttribute("item", item) 코드 주석했는데 잘 동작하는 것 확인
     * 모델에 담을 때는 이름이 필요하므로 @ModelAttribute로 지정한 name(value)속성을 사용
     *
     * ex)
     * @ModelAttribute("hello") Item item -> 이름을 hello로 지정
     * model.attribute("hello", item) - >모델에 hello 이름으로 저장
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item , Model model){ // ModelAttribute에 있는 "item"은 view에서도 쓰이기 때문에 view에 쓰이는 것과 똑같게 적어줄 것

        itemRepository.save(item);

//        model.addAttribute("item", item); // 자동 추가, 생략 가능

        return "basic/item"; // 상품 저장하면 상품 상세페이지에서 보여주려고
    }

    /**
     * @ModelAttribute에 name을 생략하면 어떻게 될까?
     * 클래스명 Item 을 첫 글자를 소문자로만 바꿔서 model.attribute 에 담김
     * Item -> item -> model.attribute("item", item)
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item ){ // Model 생략 가능 (@ModelAttribute 가 자동으로 해주므로)

        itemRepository.save(item);

        return "basic/item"; // 상품 저장하면 상품 상세페이지에서 보여주려고
    }

    /**
     * @ModelAttribute 생략 가능
     * String이 아닌 직접 만든 임의의 객체인 경우에는 @ModelAttribute 가 자동으로 적용
     * 여기서는 Item인 임의의 객체이므로 @ModelAttribute 가 자동으로 적용
     * String인 경우에는 @RequestParam 자동 적용
     *
     *  @ModelAttribute 가 생략된 경우에도 Item -> item으로 자동 적용되어 model.attribute("item", item) 자동 추가 됨
     */
    //@PostMapping("/add")
    public String addItemV4( Item item){

        itemRepository.save(item);

        return "basic/item"; // 상품 저장하면 상품 상세페이지에서 보여주려고
    }

    /**
     * PRG 패턴 이용
     * 앞서 만들었던 /add 컨트롤러들은 post하고 뷰 템플릿으로 보여주기 때문에 마지막 url은 계속 /add로 남아있다.
     * 그래서 새로고침을 하면 데이터가 계속 중복되어 생성되는 문제가 발생
     * redirect 로 상품 상세페이지로 이동시키면 url을 바꿔버리는 것이기 때문에 /add가 아닌   /{itemId}이 된다.
     * 그러나 "redirect:/basic/items/" + item.getId(); 이런식으로 URL에 변수를 더해서 사용하는 것은 URL인코딩이 안되기 때문에 위험
     * RedirectAttribute 를 사용해서 해결
     */
    @PostMapping("/add")
    public String addItemV5( Item item){

        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId , Model model){

        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }



    /**
     * 리다이렉트
     * 상품 수정은 뷰 템플릿을 호출하는 대신에 상품 상세화면응로 이동하도록 리다이렉트 호출
     * redirect:/... 으로 편리하게 리다이렉트 지원
     * "redirect:/basic/items/{itemId}"
     * 컨트롤러에 매핑된 @PathVariable의 값은 redirect에도 사용할 수 있음
     * {itemId}의 값은 @PathVariable Long itemId의 값을 그대로 사용
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId , @ModelAttribute Item item){

        itemRepository.update(itemId , item);

        return "redirect:/basic/items/{itemId}";
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
