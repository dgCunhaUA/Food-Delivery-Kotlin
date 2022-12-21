package pt.ua.cm.fooddelivery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.network.model.Client
import pt.ua.cm.fooddelivery.network.request.LoginRequest
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.repository.UserRepository
import pt.ua.cm.fooddelivery.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<Client>> = MutableLiveData()

    private val api = Api
    private val sessionManager = SessionManager(getApplication<Application>().baseContext)

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
                            //sessionManager.saveAuthToken(this@LoginViewModel, client.token)
                            sessionManager.saveAuthToken(client.token)
                            loginResult.postValue(BaseResponse.Success(client))
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