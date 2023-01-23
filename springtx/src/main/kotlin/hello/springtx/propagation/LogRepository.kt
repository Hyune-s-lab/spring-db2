package hello.springtx.propagation

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager

@Repository
class LogRepository(private val em: EntityManager) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun save(logMessage: Log) {
        log.info("### log 저장")
        em.persist(logMessage)
        if (logMessage.message.contains("로그예외")) {
            log.info("### log 저장시 예외 발생")
            throw RuntimeException("예외 발생")
        }
    }

    fun find(message: String): Optional<Log> {
        return em.createQuery<Log>("select l from Log l where l.message = :message", Log::class.java)
            .setParameter("message", message)
            .resultList.stream().findAny()
    }
}
