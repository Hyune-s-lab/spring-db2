package hello.itemservice

import hello.itemservice.config.MemoryConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(value = [MemoryConfig::class, TestDataInit::class])
@SpringBootApplication(scanBasePackages = ["hello.itemservice.web"])
class ItemserviceApplication

fun main(args: Array<String>) {
    runApplication<ItemserviceApplication>(*args)
}
