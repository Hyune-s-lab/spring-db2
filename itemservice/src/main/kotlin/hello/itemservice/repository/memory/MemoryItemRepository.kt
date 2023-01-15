package hello.itemservice.repository.memory

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCond
import hello.itemservice.repository.ItemUpdateDto
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MemoryItemRepository : ItemRepository {
    override fun save(item: Item): Item {
        item.id = ++sequence
        store[item.id!!] = item
        return item
    }

    override fun update(itemId: Long, updateParam: ItemUpdateDto) {
        val findItem: Item = findById(itemId).orElseThrow()
        findItem.itemName = updateParam.itemName
        findItem.price = updateParam.price
        findItem.quantity = updateParam.quantity
    }

    override fun findById(id: Long): Optional<Item> {
        return Optional.ofNullable<Item>(store[id])
    }

    override fun findAll(cond: ItemSearchCond): List<Item> {
        val itemName: String? = cond.itemName
        val maxPrice: Int? = cond.maxPrice
        return store.values.stream()
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
        private val store: MutableMap<Long, Item> = HashMap<Long, Item>() // static
        private var sequence = 0L // static
    }
}
