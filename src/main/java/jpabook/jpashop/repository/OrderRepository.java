package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    /**
     * 주문 저장
     */
    public void save(Order order) {
        em.persist(order);
    }

    /**
     * 주문 단건 조회
     */
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        // JPA - JPQL 동적 쿼리
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false; // where문 첫 주입 후 두번째 조건이 존재한다면 and조건이 들어가야하므로 false로 초기화
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false; // where문 주입 후 컨디션 false
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);//최대 1000건 조회

        if (orderSearch.getOrderStatus() != null)
            query = query.setParameter("status", orderSearch.getOrderStatus());
        if (StringUtils.hasText(orderSearch.getMemberName()))
            query = query.setParameter("name", orderSearch.getMemberName()); //Order와 연관된 member를 조인하는 join쿼리. o.member m 으로 표현한다.

        return query.getResultList();
    }

    /**
     * JPA Criteria 기능
     * 단점 : 유지보수성 ZERO 가독성 똥망
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class); // from 타입 Order Alias o 지정
        Join<Object, Object> m = o.join("member", JoinType.INNER); //조인 타입 Member - Alias m 지정
        List<Predicate> criteria = new ArrayList<>(); // Predicate : 동적쿼리에 대한 컨디션 조합을 깔끔하게 생성할 수 있다.

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus()); // 두 값이 일치하는지에 대한 조건 o.status =: status(파라미터)
            criteria.add(status);
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);//최대 1000건 조회
        return query.getResultList();


    }
}
