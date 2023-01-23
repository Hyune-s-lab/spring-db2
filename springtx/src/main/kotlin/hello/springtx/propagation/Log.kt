package hello.springtx.propagation

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Log {
    @Id
    @GeneratedValue
    val id: Long? = null
    var message: String = ""

    constructor()
    constructor(message: String) {
        this.message = message
    }
}
