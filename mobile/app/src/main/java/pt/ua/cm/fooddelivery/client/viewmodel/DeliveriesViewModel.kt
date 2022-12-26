package pt.ua.cm.fooddelivery.client.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import pt.ua.cm.fooddelivery.client.repository.OrderRepository
import pt.ua.cm.fooddelivery.client.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class DeliveriesViewModel(private val orderRepository: OrderRepository,
                          private val userRepository: UserRepository
) : ViewModel() {

    //val orderItems: LiveData<List<Order>> = orderRepository.allUserOrders.asLiveData()
    val ordersResult: MutableLiveData<BaseResponse<List<DeliveriesResponse>>> = MutableLiveData()


    private val api = Api

    fun getAllOrders() {
        ordersResult.postValue(BaseResponse.Loading())

        try {
            CoroutineScope(Dispatchers.IO).launch {
                val currentClient: Client = userRepository.getCurrentClient()

                api.apiService.getAllOrdersById(currentClient.id).enqueue(object :
                    Callback<List<DeliveriesResponse>> {
                    override fun onFailure(call: Call<List<DeliveriesResponse>>, t: Throwable) {
                        //handle error here
                        Timber.i("error $t")
                    }

                    override fun onResponse(call: Call<List<DeliveriesResponse>>, response: Response<List<DeliveriesResponse>>) {
                        //your raw string response
                        Timber.i("response: $response")
                        Timber.i("body ${response.body()}")

                        ordersResult.postValue(BaseResponse.Success(response.body()))
                    }
                })
            }
        } catch (ex: Exception) {
            //loginResult.value = BaseResponse.Error(ex.message)
            Timber.i("Error ${ex.message}")
        }
    }

}

class DeliveriesModelFactory(private val orderRepository: OrderRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(DeliveriesViewModel::class.java))
            return DeliveriesViewModel(orderRepository, userRepository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}