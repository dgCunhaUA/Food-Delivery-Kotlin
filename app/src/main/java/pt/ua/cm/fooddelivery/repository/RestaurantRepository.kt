package pt.ua.cm.fooddelivery.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.entities.Restaurant
import pt.ua.cm.fooddelivery.entities.RestaurantWithMenus


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