package pt.ua.cm.fooddelivery.client.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.client.entities.Menu
import pt.ua.cm.fooddelivery.client.entities.OrderWithMenus
import pt.ua.cm.fooddelivery.client.entities.Restaurant
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.network.request.OrderFinishRequest
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.network.response.OrderFinishResponse
import pt.ua.cm.fooddelivery.client.repository.OrderRepository
import pt.ua.cm.fooddelivery.client.repository.RestaurantRepository
import pt.ua.cm.fooddelivery.client.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class CartViewModel(private val orderRepository: OrderRepository,
                    private val userRepository: UserRepository,
                    private val restaurantRepository: RestaurantRepository
): ViewModel()
{
    val currentCart: LiveData<OrderWithMenus> = orderRepository.currentCart

    val feedbackMessage: MutableLiveData<String?> = MutableLiveData(null)

    val cartResult: MutableLiveData<BaseResponse<OrderFinishResponse>> = MutableLiveData()

    private val api = Api

    fun getCurrentCart() {
        CoroutineScope(Dispatchers.IO).launch {
            orderRepository.getCurrentCart()
        }
    }

    fun addMenuToCart(menu: Menu) {
        CoroutineScope(Dispatchers.IO).launch {
            if (
                (currentCart.value?.menus?.isNotEmpty() == true &&
                        currentCart.value?.menus!![0].restaurantId == menu.restaurantId)
                || currentCart.value?.menus?.isEmpty() == true
            )
            {
                Timber.i("Inserting menu ${menu.menuId} in order ${currentCart.value!!.order.orderId}")
                currentCart.value?.order?.let { orderRepository.addMenuToCart(menu, it.orderId) }
                feedbackMessage.postValue("Added")
            } else {
                Timber.i("Error Inserting menu ${menu.menuId}")
                feedbackMessage.postValue("Error Inserting menu")
            }
        }
    }

    fun rmMenuFromCart(menu: Menu) {
        CoroutineScope(Dispatchers.IO).launch {
            if (currentCart.value?.menus?.any { x1 -> x1.menuId == menu.menuId}  == true) {
                Timber.i("Removing menu ${menu.menuId} from order")
                orderRepository.rmMenuFromCart(menu)
                feedbackMessage.postValue("Removed")
            } else {
                Timber.i("Error removing menu ${menu.menuId}")
                feedbackMessage.postValue("Error removing menu")
            }
        }
    }

    fun finishOrder() {
        cartResult.value = BaseResponse.Loading()
        CoroutineScope(Dispatchers.IO).launch {

            val currentClient: Client = userRepository.getCurrentClient()

            //userRepository.currentClient
            Timber.i("currentClient $currentClient")


            Timber.i("Finishing order")

            val restaurant: Restaurant = currentCart.value?.order?.restaurantId?.let {
                restaurantRepository.getRestaurantById(it)
            }!!

            Timber.i("Preparing API Request")

            val orderFinishRequest = OrderFinishRequest(
                restaurant_name = restaurant.name,
                restaurant_address = "restaurantADDRESS",
                client_id = currentClient.id.toInt(),
                client_name = currentClient.name,
                client_address = currentClient.address
            )

            Timber.i("orderFinishRequest: $orderFinishRequest")

            api.apiService.createOrder(orderFinishRequest).enqueue(object:
                Callback<OrderFinishResponse> {
                override fun onFailure(call: Call<OrderFinishResponse>, t: Throwable) {
                    //handle error here
                    Timber.i("error $t")
                    cartResult.postValue(BaseResponse.Error(t.toString()))
                }

                override fun onResponse(call: Call<OrderFinishResponse>, response: Response<OrderFinishResponse>) {
                    val order: OrderFinishResponse = response.body() as OrderFinishResponse

                    cartResult.postValue(BaseResponse.Success(order))

                    CoroutineScope(Dispatchers.IO).launch {
                        orderRepository.finishOrder()
                        Timber.i("Deleted menus from cart")
                    }
                }
            })
        }
    }
}

class CartModelFactory(private val orderRepository: OrderRepository,
                       private val userRepository: UserRepository,
                       private val restaurantRepository: RestaurantRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(CartViewModel::class.java))
            return CartViewModel(orderRepository, userRepository, restaurantRepository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}