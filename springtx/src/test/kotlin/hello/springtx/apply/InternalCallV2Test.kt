package hello.springtx.apply

import io.kotest.core.spec.style.FunSpec
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@SpringBootTest
class InternalCallV2Test(private val callService: CallService) : FunSpec({
    val log = LoggerFactory.getLogger(javaClass)

    test("printProxy()") {
        log.info("callService class={}", callService.javaClass)
    }

    test("externalCallV2()") {
        callService.external()
    }
}) {
    @TestConfiguration
    internal class InternalCallV1TestConfig {
        @Bean
        fun callService(): CallService {
            return CallService(internalService())
        }

        @Bean
        fun internalService(): InternalService {
            return InternalService()
        }
    }

    class CallService(private val internalService: InternalService) {
        private val log = LoggerFactory.getLogger(javaClass)

        fun external() {
            log.info("call external")
            printTxInfo()
            internalService.internal()
        }

        private fun printTxInfo() {
            val txActive = TransactionSynchronizationManager.isActualTransactionActive()
            log.info("tx active={}", txActive)
        }
    }

    open class InternalService {
        private val log = LoggerFactory.getLogger(javaClass)

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
