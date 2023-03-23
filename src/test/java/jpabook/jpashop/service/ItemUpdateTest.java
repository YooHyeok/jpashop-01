package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemUpdateTest {
    
    @PersistenceContext
    EntityManager em; 
    
    @Test
    public void updateTest() throws Exception {

        Book book = em.find(Book.class, 1L);

        //Tx
        book.setName("asdasda"); // 이름 변경

        //Tx commit - DirtyChecking : Transaction commit시점에 엔티티의 변경을 감지해서 updateQuery를 날려준다.

    }
}
