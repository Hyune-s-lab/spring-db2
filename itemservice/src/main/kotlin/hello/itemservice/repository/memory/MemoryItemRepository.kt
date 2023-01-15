package hello.itemservice.repository.memory

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCond
import hello.itemservice.repository.ItemUpdateDto
import org.springframework.stereotype.Repository

@Repository
class MemoryItemRepository : ItemRepository {
    override fun save(item: Item): Item {
        item.id = ++sequence
        store[item.id!!] = item
        return item
    }

    override fun update(itemId: Long, updateParam: ItemUpdateDto) {
        val findItem: Item = findById(itemId) ?: throw RuntimeException("찾을 수 없는 entity")

        findItem.itemName = updateParam.itemName
        findItem.price = updateParam.price
        findItem.quantity = updateParam.quantity
    }

    override fun findById(id: Long): Item? {
        return store[id]
    }

    override fun findAll(cond: ItemSearchCond): List<Item> {
        val itemName: String? = cond.itemName
        val maxPrice: Int? = cond.maxPrice
        return store.values
            .filter { item: Item ->
                itemName == null || item.itemName.contains(itemName)
            }.filter { item: Item ->
                maxPrice == null || item.price <= maxPrice
            }.toList()
    }

    fun clearStore() {
        store.clear()
    }

    companion object {
        private val store: MutableMap<Long, Item> = HashMap<Long, Item>()
        private var sequence = 0L
    }
}
