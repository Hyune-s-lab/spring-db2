package hello.itemservice.service

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemSearchCond
import hello.itemservice.repository.ItemUpdateDto
import java.util.*

interface ItemService {
    fun save(item: Item): Item
    fun update(itemId: Long, updateParam: ItemUpdateDto)
    fun findById(id: Long): Optional<Item>
    fun findItems(itemSearch: ItemSearchCond): List<Item>
}
