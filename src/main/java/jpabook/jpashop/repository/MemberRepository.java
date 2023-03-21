package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // component스캔의 대상이된다. @SpringBootApplication이 선언된 클래스의 패키지를 포함 하위에 있는 모든 component를 스캔한뒤 빈으로 등록해준다.
public class MemberRepository {
    @PersistenceContext //Spring이 EntityManager를 주입시켜준다. - spring data jpa에서 @Autowired나 @RequiredArgsConstructor로도 주입 가능.
    private EntityManager em;

    public void save(Member member) {
        em.persist(member); // persist : 영속성 컨텍스트에 member 객체를 주입 - transacrion이 commit되는 시점에 DB에 insert 반영
    }

    public Member findOne(Long id) { // id 기준 단건 조회 (타입, PK)
        return em.find(Member.class, id);
    }

    public List<Member> findAll() { // jqpl은 sql과 차이는 sql은 테이블을대상 jqpl은 엔티티객체를 대상으로 조회한다.
        return em.createQuery("select m from Member m", Member.class) // 1. jpql , 2.조회 타입
                .getResultList(); // 조회를 List로 반환
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) //parameter - :name에 매핑된다
                .getResultList();
    }
}
