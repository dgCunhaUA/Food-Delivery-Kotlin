package pt.ua.cm.fooddelivery.cart

import androidx.lifecycle.MutableLiveData
import pt.ua.cm.fooddelivery.menu.Menu
import timber.log.Timber

class OrderRepository(private val orderDao: OrderDao) {

    val currentCart: MutableLiveData<OrderWithMenus> = MutableLiveData<OrderWithMenus>()

    fun getCurrentCart()
    {
        currentCart.postValue(orderDao.getCurrentCart())
    }

    suspend fun addMenuToCart(menu: Menu, orderId: Int) {
        orderDao.addMenuToCart(menu.menuId, orderId)
    }

    suspend fun rmMenuFromCart(menu: Menu) {
        orderDao.rmMenuFromCart(menu.menuId)
    }
}