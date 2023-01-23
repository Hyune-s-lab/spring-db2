package hello.springtx.propagation

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Member(
    var username: String = ""
) {
    @Id
    @GeneratedValue
    val id: Long? = null
}
