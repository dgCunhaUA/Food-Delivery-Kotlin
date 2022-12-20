package pt.ua.cm.fooddelivery.repository

import androidx.lifecycle.MutableLiveData
import pt.ua.cm.fooddelivery.entities.OrderWithMenus
import pt.ua.cm.fooddelivery.entities.Menu

class OrderRepository(private val orderDao: OrderDao) {

    val currentCart = MutableLiveData<OrderWithMenus>()

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
}