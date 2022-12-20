package pt.ua.cm.fooddelivery.adapter

import pt.ua.cm.fooddelivery.entities.Menu

interface MenuItemClickListener {
    fun addMenuToCart(menu: Menu)
    fun rmMenuFromCart(menu: Menu)
}