package hello.springtx.propagation

import io.kotest.core.spec.style.FunSpec
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.UnexpectedRollbackException
import org.springframework.transaction.interceptor.DefaultTransactionAttribute
import javax.sql.DataSource

@SpringBootTest
class BasicTxTest(private val txManager: PlatformTransactionManager) : FunSpec({
    val log = LoggerFactory.getLogger(javaClass)

    test("commit()") {
        log.info("### 트랜잭션 시작")
        val status: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())

        log.info("### 트랜잭션 커밋 시작")
        txManager.commit(status)
        log.info("### 트랜잭션 커밋 완료")
    }

    test("rollback()") {
        log.info("### 트랜잭션 시작")
        val status: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### 트랜잭션 롤백 시작")
        txManager.rollback(status)
        log.info("### 트랜잭션 롤백 완료")
    }

    test("double_commit()") {
        log.info("### 트랜잭션1 시작")
        val tx1: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### 트랜잭션1 커밋")
        txManager.commit(tx1)
        log.info("### 트랜잭션2 시작")
        val tx2: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### 트랜잭션2 커밋")
        txManager.commit(tx2)
    }

    test("double_commit_rollback()") {
        log.info("### 트랜잭션1 시작")
        val tx1: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### 트랜잭션1 커밋")
        txManager.commit(tx1)
        log.info("### 트랜잭션2 시작")
        val tx2: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### 트랜잭션2 롤백")
        txManager.rollback(tx2)
    }

    test("inner_commit()") {
        log.info("### 외부 트랜잭션 시작")
        val outer: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### outer.isNewTransaction()={}", outer.isNewTransaction)
        log.info("### 내부 트랜잭션 시작")
        val inner: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### inner.isNewTransaction()={}", inner.isNewTransaction)
        log.info("### 내부 트랜잭션 커밋")
        txManager.commit(inner)
        log.info("### 외부 트랜잭션 커밋")
        txManager.commit(outer)
    }

    test("outer_rollback()") {
        log.info("### 외부 트랜잭션 시작")
        val outer: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### 내부 트랜잭션 시작")
        val inner: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### 내부 트랜잭션 커밋")
        txManager.commit(inner)
        log.info("### 외부 트랜잭션 롤백")
        txManager.rollback(outer)
    }

    test("inner_rollback()") {
        log.info("### 외부 트랜잭션 시작")
        val outer: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### 내부 트랜잭션 시작")
        val inner: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### 내부 트랜잭션 롤백")
        txManager.rollback(inner) // rollback-only 표시
        log.info("### 외부 트랜잭션 커밋")
        assertThatThrownBy { txManager.commit(outer) }
            .isInstanceOf(UnexpectedRollbackException::class.java)
    }

    test("inner_rollback_requires_new()") {
        log.info("### 외부 트랜잭션 시작")
        val outer: TransactionStatus = txManager.getTransaction(DefaultTransactionAttribute())
        log.info("### outer.isNewTransaction()={}", outer.isNewTransaction) // true
        log.info("### 내부 트랜잭션 시작")
        val definition = DefaultTransactionAttribute()
        definition.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
        val inner: TransactionStatus = txManager.getTransaction(definition)
        log.info("### inner.isNewTransaction()={}", inner.isNewTransaction) // true
        log.info("### 내부 트랜잭션 롤백")
        txManager.rollback(inner) //롤백
        log.info("### 외부 트랜잭션 커밋")
        txManager.commit(outer) //커밋
    }
}) {
    @TestConfiguration
    internal class Config {
        @Bean
        fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
            return DataSourceTransactionManager(dataSource)
        }
    }
}
