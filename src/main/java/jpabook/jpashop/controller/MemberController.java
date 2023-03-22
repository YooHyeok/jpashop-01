package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        // @Valid : javax.validation의 @NotEmpty 등 MemberForm에 선언한 어노테이션의 Validation(유효성검증) 처리 해준다.
        // BindingResult 객체에 오류가 담기고 정상적으로 실행된다.
        // thymeleaf에서 ${#fields.hasErrors('name')}를 통해 해당 필드의 오류 여부를 확인 할 수 있다. (@NotEmpty에 의해 비어있으면 오류발생 - true)
        // thymeleaf에서 th:errors="*{name}"를 통해 해당 필드의 오류 내용을 출력한다. (@NotEmpty에 입력한 오류내용 출력)
        System.out.println(result.toString());
        System.out.println(form);
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        memberService.join(member);
        
        return "redirect:/"; // 홈화면으로 redirect 새로고침
    }
}
