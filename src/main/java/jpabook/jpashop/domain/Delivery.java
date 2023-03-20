package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    @OneToOne(mappedBy = "delivery") //양방향 1대1 매핑 관계
    private Order order;
    @Embedded //내장타입 컬럼
    private Address address;

    @Enumerated(EnumType.STRING) //ORDINAL는 정수 증가값 이므로 밀릴위험이 다분하다.
    private DeliveryStatus status; //배송상태 [READY, COMP] 준비/완료
}
