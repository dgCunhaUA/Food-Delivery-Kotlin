package pt.ua.cm.fooddelivery.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.entities.Client
import pt.ua.cm.fooddelivery.entities.Restaurant
import pt.ua.cm.fooddelivery.repository.UserRepository

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {

    val currentClient: LiveData<Client?> = userRepository.currentClient.asLiveData()


    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.deleteAll()
        }
    }

}

class ProfileModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
            return ProfileViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}