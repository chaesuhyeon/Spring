package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }


    /**
     * BindingResult 순서 중요
     * public FieldError(String objectName, String field, String defaultMessage){}
     * objectName : @ModelAttribute 이름
     * field : 오류가 발생한 필드 이름
     * defaultMessage : 오류 기본 메세지
     */
    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes , Model model) {

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // 글자가 없으면
            bindingResult.addError(new FieldError("item" , "itemName" , "상품 이름은 필수입니다."));
            // FieldError : 필드 에러 검증 시 사용
            // new FieldError(Model에 넣어주는 객체, 필드명 , 오류 메세지)
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >1000000){
            bindingResult.addError(new FieldError("item" , "price" , "가격은 1,000 ~ 1,000,000 까지 허용합니다."));

        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item" , "quantity" , "수량은 최대 9,999 까지 허용합니다."));

        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item" ,"가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice ));
                // 특정 필드에러가 아니기 때문에  new ObjectError 사용

            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors ={}" , bindingResult); // bindingResult는 자동으로 view에 넘어가기 때문에 model에 넣는 로직은 생략해도 됨
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes , Model model) {

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // 글자가 없으면
            bindingResult.addError(new FieldError("item" , "itemName" , item.getItemName(),false,null,null,"상품 이름은 필수입니다."));
            // FieldError : 필드 에러 검증 시 사용
            // new FieldError(Model에 넣어주는 객체, 필드명 , 오류 메세지)
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >1000000){
            bindingResult.addError(new FieldError("item" , "price" , item.getPrice(),false, null, null,"가격은 1,000 ~ 1,000,000 까지 허용합니다."));

        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item" , "quantity" , item.getQuantity(), false, null, null,"수량은 최대 9,999 까지 허용합니다."));

        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item" ,null,null,"가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice ));
                // 특정 필드에러가 아니기 때문에  new ObjectError 사용

            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors ={}" , bindingResult); // bindingResult는 자동으로 view에 넘어가기 때문에 model에 넣는 로직은 생략해도 됨
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes , Model model) {
        
        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // 글자가 없으면
            bindingResult.addError(new FieldError("item" , "itemName" , item.getItemName(),false,new String[]{"required.item.itemName"},null,null));

        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >1000000){
            bindingResult.addError(new FieldError("item" , "price" , item.getPrice(),false, new String[]{"range.item.price"}, new Object[]{1000,1000000}, null));

        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item" , "quantity" , item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999},null));

        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item" ,new String[]{"totalPriceMin"},new Object[]{10000 , resultPrice}, null));
                // 특정 필드에러가 아니기 때문에  new ObjectError 사용

            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors ={}" , bindingResult); // bindingResult는 자동으로 view에 넘어가기 때문에 model에 넣는 로직은 생략해도 됨
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes , Model model) {

        log.info("objectName={}" , bindingResult.getObjectName()); // objectName=item
        log.info("target={}" , bindingResult.getTarget()); // target=Item(id=null, itemName=, price=null, quantity=null)

        // 검증 로직
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult , "itemName" , "required");


        // 검증 로직
/*        if(!StringUtils.hasText(item.getItemName())){ // 글자가 없으면
            bindingResult.rejectValue("itemName" , "required"); // rejectValue(필드명, 에러코드) . bindingResult는 항상 타켓 다음에 오기 때문에 검증해야할 객체를 이미 알고 있는 상태 . 그래서 rejectValue에 필드명을 바로 적어줘도 Item의 필드라는 것을 알고 있음

        }
*/

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >1000000){
            bindingResult.rejectValue("price", "range" , new Object[]{1000,1000000} , null); // rejectValue(필드명, 에러코드 , arguments , defaultMessage);
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.rejectValue("quantity" , "max" , new Object[]{9999} , null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin" , new Object[]{10000 , resultPrice} , null);

            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors ={}" , bindingResult); // bindingResult는 자동으로 view에 넘어가기 때문에 model에 넣는 로직은 생략해도 됨
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

