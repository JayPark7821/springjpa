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
    @ManyToOne(fetch = FetchType.LAZY) // order와 member는 N:1관계
    @JoinColumn(name = "member_id") // joinColumn mapping을 뭘로 할꺼냐!! 연관관계 주인쪽에 joinColumn
    private Member member;

    @OneToMany(mappedBy ="order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();


//    persist(orderItemA)
//    persist(orderItemB)
//    persist(orderItemC)
//    persist(order)
//    원래는 entity당 각각 무조건 persist해줘야한다.
//    그런데 cascade를 넣어주면
//
//    persist(order)만 해주면 된다.
//    cascade는 persist를 전파한다
//    ALL로 해놓으면 delete할때도 같이 다 지워준다.
//

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
//    order 저장할때 delivery 값을 세팅해놓으면
//    delivery값을 같이 persist해준다.
//    원래대로라면 각각 persist해줘야한다.


    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ order ,cancel]

    // == 연관관계 편의 메서드 == //
    // 로직상 핵심적으로 컨트롤 하는쪽에 들고있는것이 좋다.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // == 생성 메서드 == //

    /**
     * 연관관계가 복잡한 도메인에서는 별도에 생성 메서드가 있으면 좋다.
     *
     * 앞으로 생성하는 지점을 변경해야하면 createOrder만 바꾸면된다.
     *
     * 밖에서 Order를 new 해서 set set set 하는 방식이 아니라.
     * 생성할때부터 createOrder를 호출해야한다. -> 주문생성에관해 완결을 시킨다.
     * 앞으로 주문생성과 관련된 메소드는 createOrder만 보면 된다.
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // == 비즈니스 로직 == //

    /**
     * 주문 취소
     */
    public void cancel() {
        // 배송완료되었다면 exception
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        // 배송완료상태가 아니라면 cancel 상태로 변경
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }

    }

    // == 조회 로직 == //

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
//        int totalPrice = 0;
//        for (OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;

        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }



}
