package hello.springtx.apply

import io.kotest.core.spec.style.FunSpec
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@SpringBootTest
class TxLevelTest(private val service: LevelService) : FunSpec({
    test("orderTest()") {
        service.write()
        service.read()
    }
}) {
    @TestConfiguration
    internal class TxLevelTestConfig {
        @Bean
        fun levelService(): LevelService {
            return LevelService()
        }
    }

    @Transactional(readOnly = true)
    class LevelService {
        private val log = LoggerFactory.getLogger(javaClass)

        @Transactional(readOnly = false)
        fun write() {
            log.info("call write")
            printTxInfo()
        }

        fun read() {
            log.info("call read")
            printTxInfo()
        }

        private fun printTxInfo() {
            val txActive = TransactionSynchronizationManager.isActualTransactionActive()
            log.info("tx active={}", txActive)
            val readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly()
            log.info("tx readOnly={}", readOnly)
        }
    }
}
