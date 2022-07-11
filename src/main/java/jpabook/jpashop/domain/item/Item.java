package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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


}
