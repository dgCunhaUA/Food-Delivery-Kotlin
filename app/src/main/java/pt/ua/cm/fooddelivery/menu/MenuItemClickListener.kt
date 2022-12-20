package pt.ua.cm.fooddelivery.menu

interface MenuItemClickListener {
    fun addMenuToCart(menu: Menu)
    fun rmMenuFromCart(menu: Menu)
}