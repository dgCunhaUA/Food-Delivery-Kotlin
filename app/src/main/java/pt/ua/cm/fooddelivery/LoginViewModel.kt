package pt.ua.cm.fooddelivery

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.network.request.LoginRequest
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.client.repository.UserRepository
import pt.ua.cm.fooddelivery.rider.entities.Rider
import pt.ua.cm.fooddelivery.rider.repository.RiderRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class LoginViewModel(application: Application, private val userRepository: UserRepository, private val riderRepository: RiderRepository)
    : AndroidViewModel(application) {

    val loginResult: MutableLiveData<BaseResponse<Any>> = MutableLiveData()

    fun autoLogin() {
        Timber.i("Auto Login")

        CoroutineScope(Dispatchers.IO).launch {
            val currentClient: Client? = userRepository.getAutoLoginClient()

            Timber.i("Client: ${currentClient.toString()}")
            if (currentClient != null) {
                Timber.i("Logging with $currentClient")
                loginResult.postValue(BaseResponse.Success(currentClient))
            }


            val currentRider: Rider? = riderRepository.getAutoLoginRider()

            Timber.i("Rider: ${currentRider.toString()}")
            if (currentRider != null) {
                Timber.i("Logging with $currentRider")
                loginResult.postValue(BaseResponse.Success(currentRider))
            }
        }
    }

    fun loginClient(email: String, pwd: String) {
        loginResult.postValue(BaseResponse.Loading())
        viewModelScope.launch {

            try {
                val loginRequest = LoginRequest(
                    email = email,
                    password = pwd
                )
                Api.apiService.loginClient(loginRequest = loginRequest)
                    .enqueue(object : Callback<Client> {
                        override fun onFailure(call: Call<Client>, t: Throwable) {
                            Timber.i("error $t")
                        }

                        override fun onResponse(call: Call<Client>, response: Response<Client>) {
                            val client: Client? = response.body()
                            if (response.code() == 200 && client != null) {
                                loginResult.postValue(BaseResponse.Success(client))

                                CoroutineScope(Dispatchers.IO).launch {
                                    userRepository.insertClient(client)
                                }
                            }
                        }
                    })
            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
                Timber.i("Error ${ex.message}")
            }
        }
    }


    fun loginRider(email: String, pwd: String) {
        loginResult.postValue(BaseResponse.Loading())

        viewModelScope.launch {

            try {
                val loginRequest = LoginRequest(
                    email = email,
                    password = pwd
                )
                Api.apiService.loginRider(loginRequest = loginRequest)
                    .enqueue(object : Callback<Rider> {
                        override fun onFailure(call: Call<Rider>, t: Throwable) {
                            Timber.i("error $t")

                        }

                        override fun onResponse(
                            call: Call<Rider>,
                            response: Response<Rider>
                        ) {
                            val rider: Rider? = response.body()
                            if (response.code() == 200 && rider != null) {
                                loginResult.postValue(BaseResponse.Success(rider))

                                CoroutineScope(Dispatchers.IO).launch {
                                    riderRepository.insertRider(rider)
                                }
                            }
                        }
                    })

            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
                Timber.i("Error ${ex.message}")
            }
        }

    }

}

class LoginModelFactory(private val application: Application,
                        private val userRepository: UserRepository,
                        private val riderRepository: RiderRepository)
    : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(application, userRepository, riderRepository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}