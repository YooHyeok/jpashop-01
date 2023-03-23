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
            /**
             * 준영속 엔티티란 영속성 컨텍스트가 더이상 관리하지 않는 엔티티를 말한다.
             * ItemConroller에서 updateItem()을 통해 값 수정을 시도하는 Book객체를 말한다.
             * Book객체는 이미  DB에 한번 저장되어서 식별자(PK)가 존재한다.
             * 이렇게 임의로 만들어 낸 엔티티도 기존 식별자를 가지고 있다면 준영속 엔티티로 볼 수 있다.
             *
             * # 준영속 엔티티 수정 2가지 방법 # : [변경감지 기능 / 병합(merge) 기능]
             * 병합감지기능 : 트랜잭션 안에서 엔티티를 다시 조회한 뒤 setter등으루 데이터를 수정 -> 트랜잭션 커밋 시점에 변경감지(DirtyChecking)
             * @Transactional
             * void update(Item itemParam) { itemParam -> 파라미터로 넘어온 준영속 상태의 엔티티
             *     Item findItem = em.find(Item.class, itemParam.getId()); -> 준영속 엔티티의 pk로 엔티티를 조회한다. -> 영속 객체로 변경
             *     findItem.setPrice(item.Param.getPrice()); -> 조회한 영속 객체의 데이터 수정 -> 변경감지 -> update sql
             *     return findItem;
             * }
             * merge(병합) : 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기술이다.
             *  Item mergeItem = em.merge(itemParam) -> 변경된 itemParam이 준영속 상태이다. merge를 통해 영속상태로 변경된다.
             *  [merge 단점] : 병합 직전 준영속 상태의 엔티티의 모든 속성이 변경된다. 이는 변경하지 않은 필드에 null이 저장될 수 있다.
             */
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
