package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;

import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() {
        // given
        Member member = createMember("회원1");

        Book book = createBook("시골 JPA", 10000, 10);

        // when
        int orderCount = 3;
        Long order = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findOne(order);
        assertThat( OrderStatus.ORDER  ).isEqualTo(findOrder.getStatus());
        assertThat(1).isEqualTo(findOrder.getOrderItems().size());
        assertThat(10000 * orderCount).isEqualTo(findOrder.getTotalPrice());
        assertThat(7).isEqualTo(book.getStockQuantity());

    }

    @Test
    void 상품주문_재고수량초과() {
        // given
        Member member = createMember("회원1");

        Book book = createBook("시골 JPA", 10000, 10);
        // when
        int orderCount = 101;


        // then
        NotEnoughStockException thrown = assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), book.getId(), orderCount));
        assertEquals("need more stock", thrown.getMessage());
    }


    @Test
    void 주문취소() {
        // given
        int orderCount = 2;
        Member member = createMember("회원1");
        Book book = createBook("시골 JPA", 10000, 10);
        Long order = orderService.order(member.getId(), book.getId(), orderCount);
        // when
        orderService.cancelOrder(order);

        // then

        Order findOrder = orderRepository.findOne(order);
        assertThat(OrderStatus.CANCEL).isEqualTo(findOrder.getStatus());
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }



    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

}