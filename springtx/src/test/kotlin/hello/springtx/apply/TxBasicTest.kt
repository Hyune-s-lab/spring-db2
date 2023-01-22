package hello.springtx.apply

import io.kotest.core.spec.style.FunSpec
import org.assertj.core.api.Assertions.assertThat
import org.slf4j.LoggerFactory
import org.springframework.aop.support.AopUtils
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@SpringBootTest
class TxBasicTest(private val basicService: BasicService) : FunSpec({
    val log = LoggerFactory.getLogger(javaClass)

    test("proxyCheck()") {
        log.info("aop class={}", basicService.javaClass);
        assertThat(AopUtils.isAopProxy(basicService)).isTrue
    }

    test("txTest()") {
        basicService.tx()
        basicService.nonTx()
    }
}) {
    @TestConfiguration
    class TxApplyBasicConfig {
        @Bean
        fun basicService(): BasicService {
            return BasicService()
        }
    }

    open class BasicService() {
        private val log = LoggerFactory.getLogger(javaClass)

        @Transactional
        open fun tx() {
            log.info("call tx");
            val txActive = TransactionSynchronizationManager.isActualTransactionActive()
            log.info("tx active={}", txActive);
        }

        open fun nonTx() {
            log.info("call nonTx");
            val txActive = TransactionSynchronizationManager.isActualTransactionActive()
            log.info("tx active={}", txActive);
        }
    }
}
