package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private int orderPrice; //주문 가격
    private int count;//주문 수량

    protected OrderItem() {
    } //다른 곳에서 createOrderItem 메소드가 아닌 new OrderItem(); 으로 사용하는 것을 막기 위해 protected 사용
    //이를 어노테이션으로 사용하면 @NoArgsConstructor(access = AccessLevel.PROTECTED)
    //인자 없는 생성자 만들기

    //==생성 메서드==//
    //orderPrice는 쿠폰이나 할인을 받을 수 있기 때문에 item의 가격이 아닌, 따로 orderPrice를 받는게 좋다
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);

    }

    //==조회 로직==//

    /**
     * 주문 상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
