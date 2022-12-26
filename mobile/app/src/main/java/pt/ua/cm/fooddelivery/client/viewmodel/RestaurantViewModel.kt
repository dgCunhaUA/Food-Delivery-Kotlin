package pt.ua.cm.fooddelivery.client.viewmodel

import androidx.lifecycle.*
import pt.ua.cm.fooddelivery.client.entities.Restaurant
import pt.ua.cm.fooddelivery.client.repository.RestaurantRepository

class RestaurantViewModel(private val repository: RestaurantRepository): ViewModel()
{
    val restaurantItems: LiveData<List<Restaurant>> = repository.allRestaurants.asLiveData()

}

class RestaurantModelFactory(private val repository: RestaurantRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(RestaurantViewModel::class.java))
            return RestaurantViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}