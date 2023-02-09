package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity{

    @Id @GeneratedValue // auto
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;

//    private String city;
//    private String street;
//    private String zipcode;
    @Embedded // 값타입이라는 것을 명시
    private Address address;


    @OneToMany(mappedBy = "member") // Order에서 ManyToOne의 member지정(Order의 member가 주인)
    private List<Order> orders = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
