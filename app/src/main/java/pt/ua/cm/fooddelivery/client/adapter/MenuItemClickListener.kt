package pt.ua.cm.fooddelivery.client.adapter

import pt.ua.cm.fooddelivery.client.entities.Menu


interface MenuItemClickListener {
    fun addMenuToCart(menu: Menu)
    fun rmMenuFromCart(menu: Menu)
}