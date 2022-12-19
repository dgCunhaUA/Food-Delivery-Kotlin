package pt.ua.cm.fooddelivery.restaurant

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class RestaurantRepository(private val restaurantDao: RestaurantDao)
{
    val allRestaurants: Flow<List<Restaurant>> = restaurantDao.getAll()

    @WorkerThread
    suspend fun insert(restaurant: Restaurant)
    {
        restaurantDao.insert(restaurant)
    }
}