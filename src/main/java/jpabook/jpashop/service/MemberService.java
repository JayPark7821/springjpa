package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional(readOnly =true) // 읽기 전용이면 조회하는곳에서 성능을 최적화 한다. 영속성 컨텍스트를 flush안하고 더티체킹을 안하고
@RequiredArgsConstructor
public class MemberService {

    /**
     * 필드주입은 test때나 필요할때 바꿀수가 없다.
      */
//    @Autowired
//    private MemberRepository memberRepository;
    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional // 기본은 readOnly = false
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }


    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        // count로 해서 0보다 크면 으로 최적화
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }


    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
