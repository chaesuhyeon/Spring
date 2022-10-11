package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
@Slf4j
public class FormItemController {

    private final ItemRepository itemRepository;

    // 코드의 반복을 줄이기 위해서 선언 -> 어느 메서드이든지, FormItemController가 호출될때마다 모델에 담김
    @ModelAttribute("regions" ) //  FormItemController을 호출할 때마다 ModelAttribute에 자동으로 addAttribute돼서 모델에 무조건 담김
    public Map<String, String> regions(){
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL" , "서울");
        regions.put("BUSAN" , "부산");
        regions.put("JEJU" , "제주");
        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes(){
//        ItemType[] values = ItemType.values(); ->  ItemType.values면 ENUM에 있는 값들을 배열로 반환해줌 [BOOK,FOOD,ETC]
//        return values;
        return ItemType.values(); // 위 코드를 inline으로 리펙토링
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST" , "빠른배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL" , "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW" , "느린 배송"));
        return deliveryCodes;
        
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        // 위에서 @ModelAttribute("regions" ) 해줬기 때문에 생략 가능
//        Map<String, String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL" , "서울");
//        regions.put("BUSAN" , "부산");
//        regions.put("JEJU" , "제주");
//        model.addAttribute("regions", regions);

        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());

        // 위에서 @ModelAttribute("regions" ) 해줬기 때문에 생략 가능
//        Map<String, String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL" , "서울");
//        regions.put("BUSAN" , "부산");
//        regions.put("JEJU" , "제주");
//        model.addAttribute("regions", regions);


        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}" , item.getOpen()); // ex) item.open=false
        log.info("item.regions={}" , item.getRegions()); // ex) item.regions=[SEOUL, JEJU] , item.regions=[]
        log.info("item.itemType={}" , item.getItemType()); // ex) item.itemType=BOOK , item.itemType=null

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        // 위에서 @ModelAttribute("regions" ) 해줬기 때문에 생략 가능
//        Map<String, String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL" , "서울");
//        regions.put("BUSAN" , "부산");
//        regions.put("JEJU" , "제주");
//        model.addAttribute("regions", regions);


        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}

