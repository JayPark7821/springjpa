package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
//@Setter Setter를 가지고 값을 변경하는게 아니라 비즈니스 로직으로 값을 변경 (ex_ addStock , removeStock)
// 전략을 부모테이블에 선언해야한다.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype") // 전략이 singletable일때 구분값을 컬럼명을 뭘로 할꺼냐!!!!
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantuty;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // == 비즈니스 로직==//

    /**
     * entity 자체에서 해결가능한 로직은
     * entity 안에 비지니스 로직을 넣는것이 좋다
     * 데이터를 가지고있는 쪽에서 비지니스 로직이 나가는것이
     * 응집도가 있다
     **/

    /**
     * stock 증가.
     */
    public void addStock(int quantity) {
        this.stockQuantuty += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantuty - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantuty = restStock;
    }
}
