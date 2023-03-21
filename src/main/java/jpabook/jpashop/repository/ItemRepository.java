package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item); // 상품(item)은 처음에 id가 없기 때문에 persist(insert)한다.
        } else {
            em.merge(item); //update와 비슷한 기능
        }
    }

    /**
     * 단건 조회
     */
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    /**
     * 전체 조회
     */
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
