package hello.springtx.propagation

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager

@Repository
class MemberRepository(private val em: EntityManager) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun save(member: Member) {
        log.info("### member 저장")
        em.persist(member)
    }

    fun find(username: String): Optional<Member> {
        return em.createQuery("select m from Member m where m.username = :username", Member::class.java)
            .setParameter("username", username)
            .resultList.stream().findAny()
    }
}
