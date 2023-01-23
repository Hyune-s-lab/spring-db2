package hello.springtx.propagation

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Member {
    @Id
    @GeneratedValue
    private val id: Long? = null
    private var username: String = ""

    constructor()
    constructor(username: String) {
        this.username = username
    }
}
