package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript" , script = "_this.price * _this.quantity >= 10000" , message = "10000원 넘게 입력해주세요")
public class Item2 {

    @NotNull(groups = UpdateCheck.class) // 수정 요구사항 추가 , SaveCheck : 저장할 때만 사용 / UpdateCheck : 수정할 때만 사용
    private Long id;

    //    @NotBlank(message = "공백x") // 오류메시지를 지정할 수도 있음
    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000 , groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999 , groups = SaveCheck.class)
    private Integer quantity;

    public Item2() {
    }

    public Item2(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
