package pt.ua.cm.fooddelivery.rider.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.client.entities.Order
import pt.ua.cm.fooddelivery.client.entities.Restaurant
import pt.ua.cm.fooddelivery.client.repository.RestaurantRepository
import pt.ua.cm.fooddelivery.client.viewmodel.RestaurantViewModel
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.network.request.OrderFinishRequest
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import pt.ua.cm.fooddelivery.network.response.OrderFinishResponse
import pt.ua.cm.fooddelivery.network.response.RiderOrderResponse
import pt.ua.cm.fooddelivery.rider.entities.Rider
import pt.ua.cm.fooddelivery.rider.repository.RiderRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RiderHomeViewModel(private val repository: RiderRepository): ViewModel() {
    //val currentRider: LiveData<Rider?> = repository.currentRider.asLiveData()

    //val currentOrders: LiveData<List<Order>> = repository.currentOrders.asLiveData()

    val orderResult: MutableLiveData<BaseResponse<List<DeliveriesResponse>>> = MutableLiveData()


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun getOrders() = viewModelScope.launch {
        orderResult.value = BaseResponse.Loading()

        CoroutineScope(Dispatchers.IO).launch {
            val currentRider: Rider = repository.getCurrentRider()


            Timber.i("Current Rider $currentRider")

            currentRider.id?.let {
                Api.apiService.getRiderOrdersById(it).enqueue(object :
                    Callback<List<DeliveriesResponse>> {
                    override fun onFailure(call: Call<List<DeliveriesResponse>>, t: Throwable) {
                        //handle error here
                        Timber.i("error $t")
                        orderResult.postValue(BaseResponse.Error(t.toString()))
                    }

                    override fun onResponse(
                        call: Call<List<DeliveriesResponse>>,
                        response: Response<List<DeliveriesResponse>>
                    ) {
                        val order: List<DeliveriesResponse> =
                            response.body() as List<DeliveriesResponse>

                        orderResult.postValue(BaseResponse.Success(order))
                    }
                })
            }
        }
    }
}

class RiderHomeModelFactory(private val repository: RiderRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(RiderHomeViewModel::class.java))
            return RiderHomeViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}