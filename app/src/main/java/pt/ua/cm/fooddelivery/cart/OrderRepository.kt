package pt.ua.cm.fooddelivery.cart

import androidx.lifecycle.MutableLiveData
import timber.log.Timber

class OrderRepository(private val orderDao: OrderDao) {

    val cartMenus: MutableLiveData<OrderWithMenus> = MutableLiveData<OrderWithMenus>()

    fun getCartMenus()
    {
        cartMenus.postValue(orderDao.getCartWithMenus())
        Timber.i("ETESTETET ${orderDao.getCartWithMenus()}")
    }
}