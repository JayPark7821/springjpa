package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // fk가 가까운곳이 연관관계의 주인으로 잡으면 된다.
    // orders 테이블에 fk가 있음으로 orders가 주인
    // order에 memeber값을 변경하면 member_id의 fk값이 다른맴버로 변경됨
    @ManyToOne // order와 member는 N:1관계
    @JoinColumn(name = "member_id") // joinColumn mapping을 뭘로 할꺼냐!! 연관관계 주인쪽에 joinColumn
    private Member member;

    @OneToMany(mappedBy ="order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderdate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ order ,cancel]


}
