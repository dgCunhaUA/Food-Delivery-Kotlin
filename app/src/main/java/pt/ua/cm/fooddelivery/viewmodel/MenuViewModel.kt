package pt.ua.cm.fooddelivery.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.repository.RestaurantRepository
import pt.ua.cm.fooddelivery.entities.RestaurantWithMenus

class MenuViewModel(private val repository: RestaurantRepository): ViewModel() {

    //val restaurantMenus: LiveData<RestaurantWithMenus> = repository.restaurantMenus.asLiveData()

    val restaurantMenus: LiveData<RestaurantWithMenus> = repository.restaurantMenus

    /*val liveData: LiveData<RestaurantWithMenus> = repository.liveData
    fun getData(restaurantId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getData(restaurantId)
        }
    }
     */

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    /*
    fun getRestaurantMenus(restaurantId: Int) = viewModelScope.launch {
        repository.getRestaurantMenus(restaurantId)
    }
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