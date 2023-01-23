package hello.springtx.propagation

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Log(
    var message: String = ""
) {
    @Id
    @GeneratedValue
    val id: Long? = null
}
