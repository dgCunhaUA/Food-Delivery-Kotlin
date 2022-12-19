package pt.ua.cm.fooddelivery.restaurant

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class RestaurantViewModel(private val repository: RestaurantRepository): ViewModel()
{
    val restaurantItems: LiveData<List<Restaurant>> = repository.allRestaurants.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(restaurant: Restaurant) = viewModelScope.launch {
        repository.insert(restaurant)
    }

    /*fun addTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.insertTaskItem(taskItem)
    }

    fun updateTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.updateTaskItem(taskItem)
    }

    fun setCompleted(taskItem: TaskItem) = viewModelScope.launch {
        if (!taskItem.isCompleted())
            taskItem.completedDateString = TaskItem.dateFormatter.format(LocalDate.now())
        repository.updateTaskItem(taskItem)
    } */
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