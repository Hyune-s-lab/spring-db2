package hello.itemservice.repository.jpa

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCond
import hello.itemservice.repository.ItemUpdateDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import javax.persistence.EntityManager

@Repository
@Transactional
class JpaItemRepository(private val em: EntityManager) : ItemRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(item: Item): Item {
        em.persist(item)
        return item
    }

    override fun update(itemId: Long, updateParam: ItemUpdateDto) {
        val findItem = em.find(Item::class.java, itemId)
        findItem.itemName = updateParam.itemName
        findItem.price = updateParam.price
        findItem.quantity = updateParam.quantity
    }

    override fun findById(id: Long): Item? {
        return em.find(Item::class.java, id)
    }

    override fun findAll(cond: ItemSearchCond): List<Item> {
        var jpql = "select i from Item i"
        val maxPrice = cond.maxPrice
        val itemName = cond.itemName
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where"
        }
        var andFlag = false
        val param: MutableList<Any?> = ArrayList()
        if (StringUtils.hasText(itemName)) {
            jpql += " i.itemName like concat('%',:itemName,'%')"
            param.add(itemName)
            andFlag = true
        }
        if (maxPrice != null) {
            if (andFlag) {
                jpql += " and"
            }
            jpql += " i.price <= :maxPrice"
            param.add(maxPrice)
        }
        log.info("jpql={}", jpql)
        val query = em.createQuery(jpql, Item::class.java)
        if (StringUtils.hasText(itemName)) {
            query.setParameter("itemName", itemName)
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice)
        }
        return query.resultList
    }
}
