package hello.itemservice.config

import hello.itemservice.repository.ItemRepository
import hello.itemservice.repository.jpa.JpaItemRepositoryV3
import hello.itemservice.service.ItemService
import hello.itemservice.service.ItemServiceV1
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class QuerydslConfig(private val em: EntityManager) {
    @Bean
    fun itemService(): ItemService {
        return ItemServiceV1(itemRepository())
    }

    @Bean
    fun itemRepository(): ItemRepository {
        return JpaItemRepositoryV3(em)
    }
}
