package pt.ua.cm.fooddelivery.client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.client.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File


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