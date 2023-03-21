package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 테스트케이스에 있으면 기본적으로 RollBack하기때문에 insert문을 확인할수 없다.
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    private Object expected;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member); // persist를 한다고해서 db에 insert문이 나가진 않는다. - JPA에서는 DB트랜잭션이 Commit될 때 flush가 되면서 insert가 동작된다.
        // DB트랜잭션이 commit될 때 flush가 되면서 jpa 영속성 컨텍스트에 있는 member객체가 insert문이 만들어지면서 DB에 insert문이 호출된다.

        //then
        em.flush(); //영속성 컨텍스트에 있는 변경이나 등록 내용을 데이터베이스에 반영한다. - 테스트 케이스에서 Transaction를 선언하고도 insert쿼리를 확인할 수 있다.
        assertEquals(member, memberRepository.findOne(saveId));

    }
    //    @Test
    @Test(expected = IllegalStateException.class) // Try Catch 대신 사용
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2); // IllegalStateException 예외 발생 - 중복
/*        try {
            memberService.join(member2); // IllegalStateException 예외 발생 - 중복
        } catch (IllegalStateException e) {
            //IllegalStateException 이 발생하면 catch로 넘어와 return이 된다.
            return;
        }*/
        //then
        fail("예외가 발생해야 한다.");
    }
}