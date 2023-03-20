package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded //내장타입
    private Address address;

    @OneToMany(mappedBy = "member") // Member 테이블과 Order 테이블은 일대다 양방향 관계 - 연관관계의 주인이 아니다 즉, order 테이블에 있는 member필드에 의해 mapped되었다.
    private List<Order> orders = new ArrayList<>();


}
