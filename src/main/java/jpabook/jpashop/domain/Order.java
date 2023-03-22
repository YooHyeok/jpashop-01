package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // protected 기본생성자 : 객체 생성 방지 - 다른 클래스에서 객체생성, 접근 및 set 방지를 해준다.
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Order 테이블과 Member 테이블은 양방향 다대일관계 - 여러개의 주문은 한명의 회원을 가질 수 있다.
    @JoinColumn(name="member_id") // Member테이블의 Pk인 Member_id를 Orders 테이블의 Fk 외래키로 설정한다.
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //cascade : OrderItems에 데이터만 넣어두고 Order를 저장하면 OrderItems도 함께 저장됨
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //양방향 1대1 매핑 관계
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING) //ORDINAL는 정수 증가값 이므로 밀릴위험이 다분하다.
    private OrderStatus status; //주문 상태 [ORDER, CANCEL] 주문/취소

    //==연관관계 메서드==//

    /**
     * 양방향 연관관계 설정시 Order와 Member에서 Member가 주문하면 orders 리스트에 해당 주문을 넣어줘야한다.
     * 그래야 member.getOrders()로 주문한것을 찾을 수 있고, order.getMember()로 양방향이라는 객체적으로 공유할 수 있게 된다.
     *
     * 회원테이블의 주문리스트에 주문을 넣고 주문테이블에 회원필드에 해당 회원정보를 넣는다.
     *
     * Member member = new Member();
     * Order order = new Order();
     * member.getOrders().add(order); - member(회원) 객체의 orders 리스트에 order(주문) 추가
     * order.setMember(member); - order(주문) 객체에 member(회원)을 추가
     * @param member
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this); // (양방향) orders List에 member를 추가한다.
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // ==생성 메서드== //
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { // OrderItem... 은 List<OrderItem>과 같다...
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery); //양방향
        for (OrderItem orderItem: orderItems) {
            order.addOrderItem(orderItem); //양방향 - orderItems List에 추가
        }
        order.setStatus(OrderStatus.ORDER); //주문 상태값을 주문으로 변경
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // ==주문 취소== //
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL); //주문 상태 취소로 변경
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // orderItem을 각각 Cancel 해줘야한다.(재고 수량 원상복구)
        }
    }

    // ==조회 로직== //
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice(); // 상품주문 전체 가격 누적합
        }
        return totalPrice;
    }
    /**
     * ==> stream 문법으로 처리하는 로직
     *      return orderItems.stream()
     *                .mapToInt(OrderItem::getTotalPrice)
     *                .sum();
     */
}
