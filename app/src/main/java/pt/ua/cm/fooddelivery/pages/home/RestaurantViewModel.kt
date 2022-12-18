package pt.ua.cm.fooddelivery.pages.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.ua.cm.fooddelivery.data.Restaurant
import pt.ua.cm.fooddelivery.data.RestaurantData

class RestaurantViewModel : ViewModel() {

    private var _currentRestaurant: MutableLiveData<Restaurant> = MutableLiveData<Restaurant>()
    val currentRestaurant: LiveData<Restaurant>
        get() = _currentRestaurant

    private var _restaurantData: ArrayList<Restaurant> = ArrayList()
    val restaurantData: ArrayList<Restaurant>
        get() = _restaurantData

    init {
        _restaurantData = RestaurantData.getRestaurantData()
        _currentRestaurant.value = _restaurantData[0]
    }

    fun updateCurrentRestaurant(restaurant: Restaurant) {
        _currentRestaurant.value = restaurant
    }

}