package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.exception.NotEhoughStockException;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired MemberService memberService;
    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("시골 JPA", 10000, 10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order resultOrder = orderRepository.findOne(orderId);
        //then

        assertEquals("상품 주문 시 상태는 Order", OrderStatus.ORDER, resultOrder.getStatus());
        assertEquals("주문한 상품의 종류가 일치해야 한다.", 1, resultOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000*orderCount, resultOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());

    }


    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Book testBook = createBook("testBook", 10000, 10);

        //when
        int orderCount = 4;
        Long orderId = orderService.order(member.getId(), testBook.getId(), orderCount);
        orderService.cancelOrder(orderId);
        Order resultOrder = orderRepository.findOne(orderId);

        //then
        assertEquals("상품 주문 시 상태는 CANCEL", OrderStatus.CANCEL, resultOrder.getStatus());
        assertEquals("주문 수량만큼 재고가 원복되어야 한다.", 10, testBook.getStockQuantity());
    }

    @Test(expected = NotEhoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {

        //given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);

        int orderCount = 11;
        //when

        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외가 발생해야 한다");
    }


    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강동구", "05234"));
        em.persist(member);
        return member;
    }
}