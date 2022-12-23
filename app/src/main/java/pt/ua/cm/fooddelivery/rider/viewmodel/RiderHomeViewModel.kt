package pt.ua.cm.fooddelivery.rider.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.client.entities.Restaurant
import pt.ua.cm.fooddelivery.client.repository.RestaurantRepository
import pt.ua.cm.fooddelivery.client.viewmodel.RestaurantViewModel
import pt.ua.cm.fooddelivery.rider.entities.Rider
import pt.ua.cm.fooddelivery.rider.repository.RiderRepository

class RiderHomeViewModel(private val repository: RiderRepository): ViewModel()
{
    //val currentRider: LiveData<Rider?> = repository.currentRider.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    /*fun insert(restaurant: Restaurant) = viewModelScope.launch {
        repository.insert(restaurant)
    } */
}

class RiderHomeModelFactory(private val repository: RiderRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(RestaurantViewModel::class.java))
            return RiderHomeViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}