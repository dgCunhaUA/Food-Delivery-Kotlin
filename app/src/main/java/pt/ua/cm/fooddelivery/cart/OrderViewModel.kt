package pt.ua.cm.fooddelivery.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.restaurant.Restaurant
import pt.ua.cm.fooddelivery.restaurant.RestaurantWithMenus

class OrderViewModel(private val repository: OrderRepository): ViewModel()
{

    val cartMenus: LiveData<OrderWithMenus> = repository.cartMenus

    fun getCartMenus() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getCartMenus()
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