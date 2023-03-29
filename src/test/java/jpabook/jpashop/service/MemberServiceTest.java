package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class) //junit 실행 시 스프링이랑 같이 실행한다는 어노테이션
@SpringBootTest //spring을 띄운 상태에서 테스트하겠다. 없으면 autowired 다 실패됨
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("test");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush();
        Assertions.assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 한다

        //then
        fail("예외가 발생해야 한다.");
    }
}