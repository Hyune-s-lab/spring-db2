package hello.springtx.propagation

import io.kotest.core.spec.style.FunSpec
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.UnexpectedRollbackException

@SpringBootTest
internal class MemberServiceTest(
    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
    private val logRepository: LogRepository
) : FunSpec({
    /**
     * memberService    @Transactional:OFF
     * memberRepository @Transactional:ON
     * logRepository    @Transactional:ON
     */
    test("outerTxOff_success()") {
        //given
        val username = "outerTxOff_success"

        //when
        memberService.joinV1(username)

        //when: 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent)
        assertTrue(logRepository.find(username).isPresent)
    }

    /**
     * memberService    @Transactional:OFF
     * memberRepository @Transactional:ON
     * logRepository    @Transactional:ON Exception
     */
    test("outerTxOff_fail()") {
        //given
        val username = "로그예외_outerTxOff_fail"

        //when
        assertThatThrownBy { memberService.joinV1(username) }
            .isInstanceOf(RuntimeException::class.java)

        //when: log 데이터는 롤백된다.
        assertTrue(memberRepository.find(username).isPresent)
        assertTrue(logRepository.find(username).isEmpty)
    }

    /**
     * memberService    @Transactional:ON
     * memberRepository @Transactional:OFF
     * logRepository    @Transactional:OFF
     */
    test("singleTx()") {
        //given
        val username = "singleTx"

        //when
        memberService.joinV1(username)

        //when: 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent)
        assertTrue(logRepository.find(username).isPresent)
    }

    /**
     * memberService    @Transactional:ON
     * memberRepository @Transactional:ON
     * logRepository    @Transactional:ON
     */
    test("outerTxOn_success()") {
        //given
        val username = "outerTxOn_success"

        //when
        memberService.joinV1(username)

        //when: 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent)
        assertTrue(logRepository.find(username).isPresent)
    }

    /**
     * memberService    @Transactional:ON
     * memberRepository @Transactional:ON
     * logRepository    @Transactional:ON Exception
     */
    test("outerTxOn_fail()") {
        //given
        val username = "로그예외_outerTxOn_fail"

        //when
        assertThatThrownBy { memberService.joinV1(username) }
            .isInstanceOf(RuntimeException::class.java)

        //when: 모든 데이터가 롤백된다.
        assertTrue(memberRepository.find(username).isEmpty)
        assertTrue(logRepository.find(username).isEmpty)
    }

    /**
     * memberService    @Transactional:ON
     * memberRepository @Transactional:ON
     * logRepository    @Transactional:ON Exception
     */
    test("recoverException_fail()") {
        //given
        val username = "로그예외_recoverException_fail"

        //when
        assertThatThrownBy { memberService.joinV2(username) }
            .isInstanceOf(UnexpectedRollbackException::class.java)

        //when: 모든 데이터가 롤백된다.
        assertTrue(memberRepository.find(username).isEmpty)
        assertTrue(logRepository.find(username).isEmpty)
    }

    /**
     * memberService    @Transactional:ON
     * memberRepository @Transactional:ON
     * logRepository    @Transactional:ON(REQUIRES_NEW) Exception
     */
    test("recoverException_success()") {
        //given
        val username = "로그예외_recoverException_success"

        //when
        memberService.joinV2(username)

        //when: member 저장, log 롤백
        assertTrue(memberRepository.find(username).isPresent)
        assertTrue(logRepository.find(username).isEmpty)
    }
})
