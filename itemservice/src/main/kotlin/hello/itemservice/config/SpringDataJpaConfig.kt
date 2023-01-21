package hello.itemservice.config

import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.jpa.JpaItemRepositoryV2
import hello.itemservice.repository.jpa.SpringDataJpaItemRepository
import hello.itemservice.service.ItemService
import hello.itemservice.service.ItemServiceV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringDataJpaConfig(private val springDataJpaItemRepository: SpringDataJpaItemRepository) {
    @Bean
    fun itemService(): ItemService {
        return ItemServiceV1(itemRepository())
    }

    @Bean
    fun itemRepository(): ItemRepository {
        return JpaItemRepositoryV2(springDataJpaItemRepository)
    }
}
