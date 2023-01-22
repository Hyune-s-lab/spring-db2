package hello.springtx.apply

import io.kotest.core.spec.style.FunSpec
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@SpringBootTest
class InternalCallV1Test(private val callService: CallService) : FunSpec({
    val log = LoggerFactory.getLogger(javaClass)

    test("printProxy()") {
        log.info("callService class={}", callService.javaClass)
    }

    test("internalCall()") {
        callService.internal()
    }

    test("externalCall()") {
        callService.external()
    }
}) {
    @TestConfiguration
    internal class InternalCallV1TestConfig {
        @Bean
        fun callService(): CallService {
            return CallService()
        }
    }

    open class CallService {
        private val log = LoggerFactory.getLogger(javaClass)

        open fun external() {
            log.info("call external")
            printTxInfo()
            internal()
        }

        @Transactional
        open fun internal() {
            log.info("call internal")
            printTxInfo()
        }

        private fun printTxInfo() {
            val txActive = TransactionSynchronizationManager.isActualTransactionActive()
            log.info("tx active={}", txActive)
        }
    }
}
