package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional // testcase에있으면 기본적으로rollback함
//2022-07-12 19:41:20.306  INFO 4604 --- [           main] o.s.t.c.transaction.TransactionContext   : Rolled back transaction for test: [DefaultTestContext
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
//    @Rollback(false)
    void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
        //when
        Long savedId = memberService.join(member);
        //then
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
        // 같은 transaction 안에서 같은 pk면 ==비교가 true
    }
    @Test
     void 중복_회원_예외() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        Member member1 = new Member();
        member1.setName("kim");

        //when
        memberService.join(member);

         //then
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                                            () -> memberService.join(member1));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }
}