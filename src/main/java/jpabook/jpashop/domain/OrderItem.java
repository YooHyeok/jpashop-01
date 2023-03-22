package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // protected 기본생성자 : 객체 생성 방지 - 다른 클래스에서 객체생성, 접근 및 set 방지를 해준다.
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //단방향 매핑
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    // ==생성 메서드== //
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 상품 추가
        orderItem.setOrderPrice(orderPrice); // 주문 가격 추가
        orderItem.setCount(count); // 주문 개수 추가

        item.removeStock(count); // 현재 재고 감소
        return orderItem;
    }

    // ==비즈니스 로직== //
    public void cancel() {
        getItem().addStock(count); //Item의 재고 수량을 원상복구 시킨다.
    }

    // ==조회 로직== //
    /**
     * 주문 상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount(); // 주문가격 * 개수
    }
}
