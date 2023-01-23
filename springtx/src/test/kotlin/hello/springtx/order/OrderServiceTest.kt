package hello.springtx.order

import io.kotest.core.spec.style.FunSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class OrderServiceTest(
    private val orderService: OrderService,
    private val orderRepository: OrderRepository
) : FunSpec({
    val log = LoggerFactory.getLogger(javaClass)

    test("complete()") {
        //given
        val order = Order()
        order.username = "정상"

        //when
        orderService.order(order)

        //then
        val findOrder = orderRepository.findById(order.id!!).get()
        assertThat(findOrder.payStatus).isEqualTo("완료")
    }

    test("runtimeException()") {
        //given
        val order = Order()
        order.username = "예외"

        //when
        assertThatThrownBy { orderService.order(order) }
            .isInstanceOf(RuntimeException::class.java)

        //then
        val orderOptional = orderRepository.findById(order.id!!)
        assertThat(orderOptional.isEmpty).isTrue
    }

    test("bizException()") {
        //given
        val order = Order()
        order.username = "잔고부족"

        //when
        try {
            orderService.order(order)
        } catch (e: NotEnoughMoneyException) {
            log.info("### 고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내")
        }

        //then
        val findOrder = orderRepository.findById(order.id!!).get()
        assertThat(findOrder.payStatus).isEqualTo("대기")
    }
})
