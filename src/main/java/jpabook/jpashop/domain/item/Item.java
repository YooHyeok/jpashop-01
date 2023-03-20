package jpabook.jpashop.domain.item;

import jpabook.jpashop.Category;
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
}
