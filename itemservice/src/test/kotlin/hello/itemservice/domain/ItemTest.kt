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
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class ItemTest(private val itemRepository: ItemRepository) : FunSpec({
    val item1 = Item("itemA", 10000, 10)
    var item2 = Item("item1", 10000, 10)
    val item3 = Item("itemA-1", 10000, 10)
    val item4 = Item("itemA-2", 20000, 20)
    val item5 = Item("itemB-1", 30000, 30)

    test("save") {
        //given

        //when
        val savedItem: Item = itemRepository.save(item1)

        //then
        val findItem: Item = itemRepository.findById(item1.id!!)!!
        assertThat(findItem).isEqualTo(savedItem)
    }

    test("updateItem") {
        //given
        val savedItem = itemRepository.save(item2)
        val itemId = savedItem.id

        //when
        val updateParam = ItemUpdateDto("item2", 20000, 30)
        itemRepository.update(itemId!!, updateParam)

        //then
        val findItem = itemRepository.findById(itemId)!!
        assertThat(findItem.itemName).isEqualTo(updateParam.itemName)
        assertThat(findItem.price).isEqualTo(updateParam.price)
        assertThat(findItem.quantity).isEqualTo(updateParam.quantity)
        item2 = findItem
    }

    test("findItems") {
        //given
        itemRepository.save(item3)
        itemRepository.save(item4)
        itemRepository.save(item5)

        //둘 다 없음 검증
        test(itemRepository, null, null, item1, item2, item3, item4, item5)
        test(itemRepository, "", null, item1, item2, item3, item4, item5)

        //itemName 검증
        test(itemRepository, "itemA", null, item1, item3, item4)
        test(itemRepository, "temA", null, item1, item3, item4)
        test(itemRepository, "itemB", null, item5)

        //maxPrice 검증
        test(itemRepository, null, 10000, item1, item3)

        //둘 다 있음 검증
        test(itemRepository, "itemA", 10000, item1, item3)
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
