package jpabook.jpashop.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@ToString
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.") // 값이 비어있으면 오류발생.
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
