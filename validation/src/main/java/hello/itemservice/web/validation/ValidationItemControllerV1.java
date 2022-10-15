package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class  ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }


    /**
     *  검증로직 추가
     *  검증에 실패하면 addForm으로 다시 보낸다
     *  검증에 실패할 경우 input에 적은 값이 유지된 채로 addForm이 다시 보여짐
     *  이유는 @GetMapping("/add") 할때 빈 객체 new Item()을 보내서 addForm에서 th:object를 사용했음
     *  th:field값에서 각 name값 존재 -> input에 입력할 시 @PostMapping("/add")으로 요청이 가서, @ModelAttribute때문에 Item에 값이 그대로 들어간채로 addForm으로 다시 보내짐
     *  addForm으로 다시 보내질 때 Item에 값이 들어간채로 다시 뿌려지기 때문에 input에 적었던 값이 사라지지 않고 그대로 남아있음
     */ 
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes , Model model) {

        // 검증 오류 결과를 보관
        Map<String, String> errors = new HashMap<>();

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // 글자가 없으면
            errors.put("itemName", "상품 이름은 필수입니다.");
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >1000000){
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999){
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if(!errors.isEmpty()){
            log.info("errors ={}" , errors);
            model.addAttribute("errors", errors); // 오류메세이가 하나라도 있으면 오류메시지를 출력하기 위해 model에 errors를 담고 입력 폼이 있는 뷰 템플릿으로 보냄
            return "validation/v1/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

