package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) // Order 테이블과 Member 테이블은 양방향 다대일관계 - 여러개의 주문은 한명의 회원을 가질 수 있다.
    @JoinColumn(name="member_id") // Member테이블의 Pk인 Member_id를 Orders 테이블의 Fk 외래키로 설정한다.
    private Member member;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY) //양방향 1대1 매핑 관계
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    private LocalDateTime orderDate; //주문 시간
    @Enumerated(EnumType.STRING) //ORDINAL는 정수 증가값 이므로 밀릴위험이 다분하다.
    private OrderStatus status; //주문 상태 [ORDER, CANCEL] 주문/취소
}
