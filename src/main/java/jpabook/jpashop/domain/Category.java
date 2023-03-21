package jpabook.jpashop;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(name = "category_item" // 중간 테이블 category_item
            , joinColumns = @JoinColumn(name = "category_id") // category테이블의 fk
            , inverseJoinColumns = @JoinColumn(name = "item_id") // Item 테이블의 fk
    ) // Item과 Category의 컬렉션 관계를 양쪽에 가지고 있을수 없으므로 일대다 다대일로 풀어내는 중간 테이블이 있어야 한다.
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Category parent;
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


}
