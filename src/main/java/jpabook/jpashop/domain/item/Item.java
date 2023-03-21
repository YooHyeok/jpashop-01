package jpabook.jpashop.domain.item;

import jpabook.jpashop.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계 매핑 전략 지정(싱글테이블 전략 - Entity가 3개로 나뉘어 따로 관리되지만 부모테이블인 Item테이블 하나로 정의된다.)
@DiscriminatorColumn(name = "dtype") //Book, Album, Movie 엔티티의 @DiscriminatorValue의 구분값을 통해 구분
@Getter @Setter
public abstract class Item {
    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity; //재고 수량
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //== 비즈니스 로직 ==// - 도메인 주도 설계에서는 데이터를 소유하고 있는 엔티티 안에 비즈니스 로직을 넣어주는게 좋다. (높은 응집도 가진다)
    /**
     * stock - 재고 수량 증가 로직
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    /**
     * stock - 재고 수량 감소 로직
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity; // 현재 남은 수량 - 주문 수량
        if (restStock < 0) { // 기본 재고 수량은 음수가 되면 안된다.
            throw new NotEnoughStockException("need more Stock");
        }
        this.stockQuantity = restStock; // 기본 재고 수량이 음수보다 클 경우 정상적으로 처리.
    }
}
