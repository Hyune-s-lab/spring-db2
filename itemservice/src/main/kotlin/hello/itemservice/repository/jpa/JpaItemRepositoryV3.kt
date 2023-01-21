package hello.itemservice.repository.jpa

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import hello.itemservice.domain.Item
import hello.itemservice.domain.QItem
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCond
import hello.itemservice.repository.ItemUpdateDto
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import javax.persistence.EntityManager

@Repository
@Transactional
class JpaItemRepositoryV3(
    private val em: EntityManager,
    private val query: JPAQueryFactory = JPAQueryFactory(em)
) : ItemRepository {
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

    fun findAllOld(cond: ItemSearchCond): List<Item> {
        val itemName = cond.itemName
        val maxPrice = cond.maxPrice
        val item = QItem.item
        val builder = BooleanBuilder()
        if (StringUtils.hasText(itemName)) {
            builder.and(item.itemName.like("%$itemName%"))
        }
        if (maxPrice != null) {
            builder.and(item.price.loe(maxPrice))
        }
        return query
            .select(item)
            .from(item)
            .where(builder)
            .fetch()
    }

    override fun findAll(cond: ItemSearchCond): List<Item> {
        val itemName = cond.itemName
        val maxPrice = cond.maxPrice
        return query
            .select(QItem.item)
            .from(QItem.item)
            .where(likeItemName(itemName), maxPrice(maxPrice))
            .fetch()
    }

    private fun likeItemName(itemName: String?): BooleanExpression? {
        return if (StringUtils.hasText(itemName)) {
            QItem.item.itemName.like("%$itemName%")
        } else null
    }

    private fun maxPrice(maxPrice: Int?): BooleanExpression? {
        return if (maxPrice != null) {
            QItem.item.price.loe(maxPrice)
        } else null
    }
}
