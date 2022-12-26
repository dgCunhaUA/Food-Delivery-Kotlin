package pt.ua.cm.fooddelivery.rider.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.client.repository.UserRepository
import pt.ua.cm.fooddelivery.client.viewmodel.ProfileViewModel
import pt.ua.cm.fooddelivery.rider.entities.Rider
import pt.ua.cm.fooddelivery.rider.repository.RiderRepository

class RiderProfileViewModel(private val repository: RiderRepository): ViewModel() {

    val currentRider: LiveData<Rider?> = repository.currentRider.asLiveData()

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAll()
        }
    }
}

class RiderProfileModelFactory(private val repository: RiderRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(RiderProfileViewModel::class.java))
            return RiderProfileViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}