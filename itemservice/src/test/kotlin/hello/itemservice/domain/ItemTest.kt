package hello.itemservice.domain

import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.ItemSearchCond
import hello.itemservice.repository.ItemUpdateDto
import hello.itemservice.repository.memory.MemoryItemRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ItemTest(private val itemRepository: ItemRepository) : FunSpec({
    test("save") {
        //given
        val item = Item("itemA", 10000, 10)

        //when
        val savedItem: Item = itemRepository.save(item)

        //then
        val findItem: Item = itemRepository.findById(item.id!!)!!
        assertThat(findItem).isEqualTo(savedItem)
    }

    test("updateItem") {
        //given
        val item = Item("item1", 10000, 10)
        val savedItem = itemRepository.save(item)
        val itemId = savedItem.id

        //when
        val updateParam = ItemUpdateDto("item2", 20000, 30)
        itemRepository.update(itemId!!, updateParam)

        //then
        val findItem = itemRepository.findById(itemId)!!
        assertThat(findItem.itemName).isEqualTo(updateParam.itemName)
        assertThat(findItem.price).isEqualTo(updateParam.price)
        assertThat(findItem.quantity).isEqualTo(updateParam.quantity)
    }

    test("findItems") {
        //given
        val item1 = Item("itemA-1", 10000, 10)
        val item2 = Item("itemA-2", 20000, 20)
        val item3 = Item("itemB-1", 30000, 30)

        itemRepository.save(item1)
        itemRepository.save(item2)
        itemRepository.save(item3)

        //둘 다 없음 검증
        test(itemRepository, null, null, item1, item2, item3)
        test(itemRepository, "", null, item1, item2, item3)

        //itemName 검증
        test(itemRepository, "itemA", null, item1, item2)
        test(itemRepository, "temA", null, item1, item2)
        test(itemRepository, "itemB", null, item3)

        //maxPrice 검증
        test(itemRepository, null, 10000, item1)

        //둘 다 있음 검증
        test(itemRepository, "itemA", 10000, item1)
    }
}) {
    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        super.afterEach(testCase, result)

        //MemoryItemRepository 의 경우 제한적으로 사용
        if (itemRepository is MemoryItemRepository) {
            itemRepository.clearStore()
        }
    }
}

fun test(itemRepository: ItemRepository, itemName: String?, maxPrice: Int?, vararg items: Item) {
    val result: List<Item> = itemRepository.findAll(ItemSearchCond(itemName, maxPrice))
    assertThat(result).containsExactly(*items)
}
