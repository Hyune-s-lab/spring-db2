package hello.itemservice.domain

data class Item(
    var id: Long? = null,
    var itemName: String = "",
    var price: Int = 0,
    var quantity: Int = 0,
) {
    constructor(itemName: String, price: Int, quantity: Int) : this(null, itemName, price, quantity)
}
