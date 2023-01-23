package hello.springtx.order

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "orders")
class Order(
    var username: String? = null, //정상, 예외, 잔고부족
    var payStatus: String? = null //대기, 완료
) {
    @Id
    @GeneratedValue
    val id: Long? = null
}
