package pt.ua.cm.fooddelivery.restaurant

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.menu.Menu
import timber.log.Timber


class RestaurantRepository(private val restaurantDao: RestaurantDao)
{
    val allRestaurants: Flow<List<Restaurant>> = restaurantDao.getAll()

    val restaurantMenus: MutableLiveData<RestaurantWithMenus> = MutableLiveData<RestaurantWithMenus>()

    //@WorkerThread
    fun getRestaurantMenus(restaurantId: Int)
    {
        //restaurantMenus = restaurantDao.getRestaurantWithMenus(restaurantId)
        restaurantMenus.postValue(restaurantDao.getRestaurantWithMenus(restaurantId))
    }
}