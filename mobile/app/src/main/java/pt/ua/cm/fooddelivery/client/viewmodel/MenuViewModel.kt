package pt.ua.cm.fooddelivery.client.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.client.entities.RestaurantWithMenus
import pt.ua.cm.fooddelivery.client.repository.RestaurantRepository

class MenuViewModel(private val repository: RestaurantRepository): ViewModel() {


    val restaurantMenus: LiveData<RestaurantWithMenus> = repository.restaurantMenus

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun getRestaurantMenus(restaurantId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getRestaurantMenus(restaurantId)
        }
    }
}

class MenuModelFactory(private val repository: RestaurantRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java))
            return MenuViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}