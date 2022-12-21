package pt.ua.cm.fooddelivery.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.MainActivity
import pt.ua.cm.fooddelivery.entities.Client
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.network.request.LoginRequest
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.repository.UserRepository
import pt.ua.cm.fooddelivery.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class LoginViewModel(application: Application, private val userRepository: UserRepository) : AndroidViewModel(application) {

    val loginResult: MutableLiveData<BaseResponse<Client>> = MutableLiveData()

    val currentClient: LiveData<Client?> = userRepository.currentClient.asLiveData()

    private val api = Api
    private val sessionManager = SessionManager(getApplication<Application>().baseContext)

    /*fun getCurrentClient(): Client? {
        /*CoroutineScope(Dispatchers.IO).launch {
            userRepository.getClient()
        }

         */
        userRepository.currentClient
    }
     */

    fun loginClient(email: String, pwd: String) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {

            try {
                val loginRequest = LoginRequest(
                    email = email,
                    password = pwd
                )
                api.apiService.loginClient(loginRequest = loginRequest).enqueue(object: Callback<Client>{
                    override fun onFailure(call: Call<Client>, t: Throwable) {
                        //handle error here
                        Timber.i("error $t")
                    }

                    override fun onResponse(call: Call<Client>, response: Response<Client>) {
                        //your raw string response
                        Timber.i("response: $response")
                        Timber.i("body ${response.body()}")

                        val client: Client? = response.body()

                        if (response.code() == 200 && client != null) {
                            sessionManager.saveAuthToken(client.token)
                            loginResult.postValue(BaseResponse.Success(client))

                            CoroutineScope(Dispatchers.IO).launch {
                                userRepository.insertClient(client)
                            }
                        } else {
                            // Error logging in
                        }
                    }
                })

            } catch (ex: Exception) {
                //loginResult.value = BaseResponse.Error(ex.message)
                Timber.i("Error ${ex.message}")
            }
        }
    }
}

class UserModelFactory(private val application: Application, private val repository: UserRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(application, repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}