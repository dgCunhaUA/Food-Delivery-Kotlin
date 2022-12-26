package pt.ua.cm.fooddelivery.client.repository

import androidx.lifecycle.MutableLiveData
import pt.ua.cm.fooddelivery.client.entities.Menu
import pt.ua.cm.fooddelivery.client.entities.Order
import pt.ua.cm.fooddelivery.client.entities.OrderWithMenus

class OrderRepository(private val orderDao: OrderDao) {

    val currentCart = MutableLiveData<OrderWithMenus>()
    //val allUserOrders = MutableLiveData<List<Order>>()


    fun getCurrentCart()
    {
        currentCart.postValue(orderDao.getCurrentCart())
    }

    suspend fun addMenuToCart(menu: Menu, orderId: Int) {
        orderDao.addMenuToCart(menu.menuId, orderId).toString()
        currentCart.postValue(orderDao.getCurrentCart())
    }

    suspend fun rmMenuFromCart(menu: Menu) {
        orderDao.rmMenuFromCart(menu.menuId)
        currentCart.postValue(orderDao.getCurrentCart())
    }

    suspend fun finishOrder() {
        val oldOrderId: Int = orderDao.getCurrentCart().order.orderId
        orderDao.makeOrderInactive()
        orderDao.insert(Order(oldOrderId+1, 0, true))
        currentCart.postValue(orderDao.getCurrentCart())
    }

    /*fun getAllUserOrders(clientId: Int)
    {
        allUserOrders.postValue(orderDao.getCurrentCart())
    }

     */
}