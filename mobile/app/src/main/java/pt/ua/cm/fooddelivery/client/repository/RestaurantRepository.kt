package pt.ua.cm.fooddelivery.client.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.client.entities.Restaurant
import pt.ua.cm.fooddelivery.client.entities.RestaurantWithMenus
import timber.log.Timber


class RestaurantRepository(private val restaurantDao: RestaurantDao)
{
    val allRestaurants: Flow<List<Restaurant>> = restaurantDao.getAll()

    val restaurantMenus: MutableLiveData<RestaurantWithMenus> = MutableLiveData<RestaurantWithMenus>()


    fun getRestaurantMenus(restaurantId: Int)
    {
        restaurantMenus.postValue(restaurantDao.getRestaurantWithMenus(restaurantId))
    }

    fun getRestaurantById(restaurantId: Int): Restaurant {
        return restaurantDao.getRestaurantById(restaurantId)
    }

}