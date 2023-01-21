package hello.itemservice

import hello.itemservice.config.QuerydslConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

//@Import(value = [MemoryConfig::class, TestDataInit::class])
//@Import(value = [JdbcTemplateV3Config::class, TestDataInit::class])
//@Import(value = [JpaConfig::class, TestDataInit::class])
//@Import(value = [SpringDataJpaConfig::class, TestDataInit::class])
@Import(value = [QuerydslConfig::class, TestDataInit::class])
@SpringBootApplication(scanBasePackages = ["hello.itemservice.web"])
class ItemserviceApplication

fun main(args: Array<String>) {
    runApplication<ItemserviceApplication>(*args)
}
