package pt.ua.cm.fooddelivery.client.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.client.adapter.MenuAdapter
import pt.ua.cm.fooddelivery.client.adapter.MenuItemClickListener
import pt.ua.cm.fooddelivery.client.entities.Menu
import pt.ua.cm.fooddelivery.client.viewmodel.CartModelFactory
import pt.ua.cm.fooddelivery.client.viewmodel.CartViewModel
import pt.ua.cm.fooddelivery.client.viewmodel.MenuModelFactory
import pt.ua.cm.fooddelivery.client.viewmodel.MenuViewModel
import pt.ua.cm.fooddelivery.databinding.FragmentRestaurantBinding
import timber.log.Timber

class RestaurantFragment : Fragment(), MenuItemClickListener {

    private val args: RestaurantFragmentArgs by navArgs()

    private lateinit var binding: FragmentRestaurantBinding

    private val menuViewModel: MenuViewModel by viewModels {
        MenuModelFactory((activity?.application as DeliveryApplication).restaurantRepository)
    }

    private val cartViewModel: CartViewModel by viewModels {
        CartModelFactory((activity?.application as DeliveryApplication).orderRepository,
            (activity?.application as DeliveryApplication).userRepository,
            (activity?.application as DeliveryApplication).restaurantRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
        menuViewModel.getRestaurantMenus(args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView")

        binding = FragmentRestaurantBinding.inflate(layoutInflater)

        setFields()
        observeFeedback()

        return binding.root
    }

    private fun setFields()
    {
        Timber.i("setRecyclerView for restaurantId: ${args.id}")
        if (activity != null) {
            menuViewModel.restaurantMenus.observe(viewLifecycleOwner) {
                binding.restaurantName.text = it.restaurant.name

                binding.menuRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity?.applicationContext)
                    adapter = MenuAdapter(it, this@RestaurantFragment)
                }
            }
            menuViewModel.getRestaurantMenus(args.id)
        }
    }

    private fun observeFeedback() {
        cartViewModel.feedbackMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                val toast = Toast.makeText(context, it, Toast.LENGTH_SHORT)
                toast.show()
            }
            cartViewModel.feedbackMessage.postValue(null)
        }
        cartViewModel.getCurrentCart()
    }

    override fun addMenuToCart(menu: Menu) {
        cartViewModel.addMenuToCart(menu)
        //orderViewModel.getCurrentCart()
        //menuViewModel.getRestaurantMenus(menu.restaurantId)
    }

    override fun rmMenuFromCart(menu: Menu) {
        cartViewModel.rmMenuFromCart(menu)
        //orderViewModel.getCurrentCart()
        //menuViewModel.getRestaurantMenus(menu.restaurantId)
    }
}