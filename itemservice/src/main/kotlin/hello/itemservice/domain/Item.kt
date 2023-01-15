package hello.itemservice.domain

class Item(
    var id: Long?,
    var itemName: String,
    var price: Int,
    var quantity: Int,
) {
    constructor(itemName: String, price: Int, quantity: Int) : this(null, itemName, price, quantity)
}
