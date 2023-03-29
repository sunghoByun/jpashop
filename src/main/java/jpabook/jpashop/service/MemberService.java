package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //데이터 변경 시 트랜잭션 안에서 수행되어야 한다!!, Spring에서 제공하는 Transactional을 사용하는게 좋다.
@RequiredArgsConstructor //final이 있는 필드만 가지고 생성자를 만들어준다.
public class MemberService {
    //필드 인젝션은 주입하기가 까다롭다
    //인젝션을 필요로 하는 변수는 final로 사용하는게 좋다
    private final MemberRepository memberRepository;

    //세터 메소드를 통해 인젝션을 하면 테스트 시 Mock 레포지토리를 인젝션 할 수 있다.
    //단점으로는 런타임 중간에 세터 메소드를 통해 인젝션을 바꾸어버릴 수 있다.(어플리케이션 동작 중간에 바뀔 수 있는 단점)
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //생성자 인젝션을 이용한다
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //회원가입
    @Transactional // 메소드의 어노테이션 우선 순위가 더 높기때문에 class에 @Transactional(readOnly=true)가 있어도 해당 메소드는 영향을 받지 않는다
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //중복 회원 검증 비즈니스 로직

        //멀티쓰레드를 고려해 DB에서 Unique 제약 조건을 걸어주는게 좋
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
