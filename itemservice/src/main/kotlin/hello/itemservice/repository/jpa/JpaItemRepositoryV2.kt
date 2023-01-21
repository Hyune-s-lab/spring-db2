package hello.itemservice.repository.jpa

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCond
import hello.itemservice.repository.ItemUpdateDto
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Repository
@Transactional
class JpaItemRepositoryV2(private val repository: SpringDataJpaItemRepository) : ItemRepository {
    override fun save(item: Item): Item {
        return repository.save(item)
    }

    override fun update(itemId: Long, updateParam: ItemUpdateDto) {
        val findItem = repository.findById(itemId).orElseThrow()
        findItem.itemName = updateParam.itemName
        findItem.price = updateParam.price
        findItem.quantity = updateParam.quantity
    }

    override fun findById(id: Long): Item? {
        return repository.findById(id).get()
    }

    override fun findAll(cond: ItemSearchCond): List<Item> {
        val itemName: String? = cond.itemName
        val maxPrice: Int? = cond.maxPrice
        return if (StringUtils.hasText(itemName) && maxPrice != null) {
//            return repository.findByItemNameLikeAndPriceLessThanEqual("%" + itemName + "%", maxPrice);
            repository.findItems("%$itemName%", maxPrice)
        } else if (StringUtils.hasText(itemName)) {
            repository.findByItemNameLike("%$itemName%")
        } else if (maxPrice != null) {
            repository.findByPriceLessThanEqual(maxPrice)
        } else {
            repository.findAll()
        }
    }
}
