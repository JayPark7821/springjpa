package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //사용자가 여러개의 주문을할 수 있다
    // 주인이아니다 거울이다

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();




}
