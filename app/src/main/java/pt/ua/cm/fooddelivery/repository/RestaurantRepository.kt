package pt.ua.cm.fooddelivery.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.entities.Client
import pt.ua.cm.fooddelivery.entities.Restaurant
import pt.ua.cm.fooddelivery.entities.RestaurantWithMenus


class RestaurantRepository(private val restaurantDao: RestaurantDao)
{
    val allRestaurants: Flow<List<Restaurant>> = restaurantDao.getAll()

    val restaurantMenus: MutableLiveData<RestaurantWithMenus> = MutableLiveData<RestaurantWithMenus>()

    //val currentRestaurant: Flow<Restaurant?> //= restaurantDao.getRestaurant()


    //@WorkerThread
    fun getRestaurantMenus(restaurantId: Int)
    {
        //restaurantMenus = restaurantDao.getRestaurantWithMenus(restaurantId)
        restaurantMenus.postValue(restaurantDao.getRestaurantWithMenus(restaurantId))
    }

    fun getRestaurantById(restaurantId: Int): Restaurant {
        return restaurantDao.getRestaurantById(restaurantId)
    }

}