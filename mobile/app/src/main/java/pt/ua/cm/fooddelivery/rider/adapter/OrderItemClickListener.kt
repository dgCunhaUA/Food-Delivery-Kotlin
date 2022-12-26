package pt.ua.cm.fooddelivery.rider.adapter

import pt.ua.cm.fooddelivery.client.entities.Menu
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse

interface OrderItemClickListener {
    fun acceptOrder(order: DeliveriesResponse)
    fun showOrderMap(order: DeliveriesResponse)
}