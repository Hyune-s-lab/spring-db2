package hello.itemservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ItemserviceApplication

fun main(args: Array<String>) {
    runApplication<ItemserviceApplication>(*args)
}
