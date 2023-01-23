package hello.springtx.apply

import io.kotest.core.spec.style.FunSpec
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.annotation.PostConstruct

@SpringBootTest
class InitTxTest(private val hello: Hello) : FunSpec({
    test("go()") {
        //초기화 코드는 스프링이 초기화 시점에 호출한다.
    }
}) {
    @TestConfiguration
    internal class InitTxTestConfig {
        @Bean
        fun hello(): Hello {
            return Hello()
        }
    }

    open class Hello {
        private val log = LoggerFactory.getLogger(javaClass)

        @PostConstruct
        @Transactional
        open fun initV1() {
            val isActive = TransactionSynchronizationManager.isActualTransactionActive()
            log.info("### Hello init @PostConstruct tx active={}", isActive)
        }

        @EventListener(ApplicationReadyEvent::class)
        @Transactional
        open fun initV2() {
            val isActive = TransactionSynchronizationManager.isActualTransactionActive()
            log.info("### Hello init ApplicationReadyEvent tx active={}", isActive)
        }
    }
}
