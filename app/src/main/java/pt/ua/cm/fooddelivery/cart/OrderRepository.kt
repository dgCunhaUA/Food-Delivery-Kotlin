package pt.ua.cm.fooddelivery.cart

import android.content.ClipData
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import pt.ua.cm.fooddelivery.menu.Menu
import timber.log.Timber

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