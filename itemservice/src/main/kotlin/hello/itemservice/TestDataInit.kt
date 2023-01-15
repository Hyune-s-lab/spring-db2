package hello.itemservice

import hello.itemservice.domain.Item
import hello.itemservice.repository.ItemRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener

@Configuration
@Profile("local")
class TestDataInit(private val itemRepository: ItemRepository) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 확인용 초기 데이터 추가
     */
    @EventListener(ApplicationReadyEvent::class)
    fun initData() {
        log.info("### test data init")
        itemRepository.save(Item("itemA", 10000, 10))
        itemRepository.save(Item("itemB", 20000, 20))
    }
}
