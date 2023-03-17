package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional //테스트에 있으면 데이터를 처리한 후 롤백으로 처리한다 - 테스트 종료 후 DB에 데이터가 존재하지 않게됨
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("테스트")
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId()).isEqualTo(saveId);
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
        /**
         * true이유는 동일한 트랜잭션 안에서 저장하고 조회할 경우 영속성 컨텍스트가 동일하다.
         * 같은 영속성 컨텍스트 안에서는 id값이 같으면 같은 엔티티로 식별한다.
         * 영속성 켄텍스트에서 식별자가 같으면 같은 엔티티로 인식한다.
         * 일차 캐시라고 불리는 곳 즉 같은 영속성 컨텍스트에 관리되고있는 동일한 엔티티가 있기 때문에 일차 캐시에서 기존에 관리하던것이 선택된다.
         * JPA 기본편 강의 참고
         */
        System.out.println("(findMember == member) = " + (findMember == member));
    }
}