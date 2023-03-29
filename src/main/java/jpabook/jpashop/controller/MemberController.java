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

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {
        //@Valid : MemberForm 객체 내에 NotEmpty 기능을 쓰겠다는 뜻
        //BindingResult : 에러 발생 시 bindingResult 객체에 에러를 담는다

        if (bindingResult.hasErrors()) {
            // 에러가 있으면 bindingResult를 createMemberForm.html까지 가지고 간다.
            /**
             *       <input type="text" th:field="*{name}" class="form-control" placeholder="이름을 입력하세요"
             *              th:class="${#fields.hasErrors('name')}? 'form-control fieldError' : 'form-control'">
             *       <p th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Incorrect date</p>
             */
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }
}
