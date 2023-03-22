package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception {

        //given
        Member member = new Member(); //회원 생성
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);

        Book book = new Book(); //책 생성
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount); //주문

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus()); //메시지, 비교할값, 실제값
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size()); //OrderItems에는 시골JPA만 들어가있다.
        assertEquals("주문 가격은 가격 * 수량 이다.", 10000 * orderCount, getOrder.getTotalPrice()); //주문 수량은 2 이고 getTotalPrice는 10000을 두번 누적 합한다.
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity()); //시골 JPA의 stockQuantity는 10 이고 주문수량은 2 이다.
    }

    @Test
    public void 주문취소() throws Exception {
        //given

        //when

        //then

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given

        //when

        //then

    }
}