package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
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

    // ==비즈니스 로직== //
    public void cancel() {
        getItem().addStock(count); //Item의 재고 수량을 원상복구 시킨다.
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount(); // 주문가격 * 개수
    }
}
