package hello.springtx.exception

import io.kotest.core.spec.style.FunSpec
import org.assertj.core.api.Assertions.*
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class RollbackTest(private val service: RollbackService) : FunSpec({
    test("runtimeException()") {
        assertThatThrownBy { service.runtimeException() }
            .isInstanceOf(RuntimeException::class.java)
    }

    test("checkedException()") {
        assertThatThrownBy { service.checkedException() }
            .isInstanceOf(MyException::class.java)
    }

    test("rollbackFor()") {
        assertThatThrownBy { service.rollbackFor() }
            .isInstanceOf(MyException::class.java)
    }
}) {
    @TestConfiguration
    internal class RollbackTestConfig {
        @Bean
        fun rollbackService(): RollbackService {
            return RollbackService()
        }
    }

    open class RollbackService {
        private val log = LoggerFactory.getLogger(javaClass)

        //런타임 예외 발생: 롤백
        @Transactional
        open fun runtimeException() {
            log.info("### call runtimeException")
            throw RuntimeException()
        }

        //체크 예외 발생: 커밋
        @Transactional
        @Throws(MyException::class)
        open fun checkedException() {
            log.info("### call checkedException")
            throw MyException()
        }

        //체크 예외 rollbackFor 지정: 롤백
        @Transactional(rollbackFor = [MyException::class])
        @Throws(
            MyException::class
        )
        open fun rollbackFor() {
            log.info("### call rollbackFor")
            throw MyException()
        }
    }

    internal class MyException : Exception()
}
