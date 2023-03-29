package jpabook.jpashop.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter

public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING) //ORDINAL을 쓰면 중간에 enumType이 추가되면 뒤쪽 타입들도 숫자가 하나씩 밀려난다.
    //따라서 스트링으로 쓰는게 좋다
    private DeliveryStatus status;




}
