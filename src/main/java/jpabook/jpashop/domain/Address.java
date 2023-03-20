package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //내장타입
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() {// jpa가 엔티티를 생성할 때 Reflection이나 Proxy기능을 사용할때가 많다. (기본생성자를 통해 생성)
    }

    public Address(String city, String street, String zipcode) { // setter를 닫아두고 객체를 생성할 때만 값을 생성하도록
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
