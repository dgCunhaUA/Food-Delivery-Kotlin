package pt.ua.cm.fooddelivery.cart

import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.menu.Menu
import timber.log.Timber
import java.util.*

class OrderViewModel(private val repository: OrderRepository): ViewModel()
{

    val currentCart: LiveData<OrderWithMenus> = repository.currentCart

    val error: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getCurrentCart() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getCurrentCart()
        }
    }

    fun addMenuToCart(menu: Menu) {
        CoroutineScope(Dispatchers.IO).launch {
            if (
                (currentCart.value?.menus?.isNotEmpty() == true &&
                        currentCart.value?.menus!![0].restaurantId == menu.restaurantId)
                || currentCart.value?.menus?.isEmpty() == true
            )
            {
                Timber.i("Inserting menu ${menu.menuId} in order ${currentCart.value!!.order.orderId}")
                currentCart.value?.order?.let { repository.addMenuToCart(menu, it.orderId) }
            } else {
                Timber.i("Error Inserting menu ${menu.menuId}")
                error.postValue(true)
            }
        }
    }

    fun rmMenuFromCart(menu: Menu) {
        CoroutineScope(Dispatchers.IO).launch {
            if (currentCart.value?.menus?.contains(menu) == true) {
                Timber.i("Removing menu ${menu.menuId} from order")
                repository.rmMenuFromCart(menu)
            } else {
                Timber.i("Error removing menu ${menu.menuId}")
            }
        }
    }
}

class OrderModelFactory(private val repository: OrderRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java))
            return OrderViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}