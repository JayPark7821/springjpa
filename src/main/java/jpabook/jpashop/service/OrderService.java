package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    // 도메인 모델 패턴 -> 비즈니스 로직 대부분이 엔티티에 들어가있다. 서비스 계층은 단순하게 엔티티에 필요한 요청을 위임하는 역할만 한다.
    // 이처럼 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // OrderItem orderItem1 = new OrderItem();
        // 로직의 통일성을 위해서 기본생성자를 protected로 만들어놓는다.

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // cascade = CascadeType.ALL 옵션으로 order만 저장해줘도 나머지 다 저장됨
        // life cycle에서 동일하게 관리할때 cascade를 사용한다.
        // order 에서만 delivery나 orderItems를 참조한다.
        // 다른곳이 참조하지 않을경우

        return order.getId();

//        itemRepository.findOne()
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }


    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllCriteria(orderSearch);
    }

}
