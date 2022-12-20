package pt.ua.cm.fooddelivery.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.cart.OrderModelFactory
import pt.ua.cm.fooddelivery.cart.OrderViewModel
import pt.ua.cm.fooddelivery.databinding.FragmentHomeBinding
import pt.ua.cm.fooddelivery.databinding.FragmentRestaurantBinding
import pt.ua.cm.fooddelivery.menu.*
import pt.ua.cm.fooddelivery.restaurant.RestaurantAdapter
import pt.ua.cm.fooddelivery.restaurant.RestaurantModelFactory
import pt.ua.cm.fooddelivery.restaurant.RestaurantViewModel
import pt.ua.cm.fooddelivery.restaurant.RestaurantWithMenus
import timber.log.Timber

class RestaurantFragment : Fragment(), MenuItemClickListener {

    private val args: RestaurantFragmentArgs by navArgs()

    private lateinit var binding: FragmentRestaurantBinding

    private val menuViewModel: MenuViewModel by viewModels {
        MenuModelFactory((activity?.application as DeliveryApplication).restaurantRepository)
    }

    private val orderViewModel: OrderViewModel by viewModels {
        OrderModelFactory((activity?.application as DeliveryApplication).orderRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
        menuViewModel.getRestaurantMenus(args.id)
        Timber.i(menuViewModel.restaurantMenus.value.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView")

        binding = FragmentRestaurantBinding.inflate(layoutInflater)

        setFields()
        observeErrors()

        return binding.root
    }

    private fun setFields()
    {
        Timber.i("setRecyclerView for restaurantId: ${args.id}")
        val mainActivity = this.activity
        if (mainActivity != null) {
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

    private fun observeErrors() {
        orderViewModel.error.observe(viewLifecycleOwner) {
            Timber.i(it.toString())

            if(it == true) {
                val toast = Toast.makeText(context, "Error Adding Menu", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        orderViewModel.getCurrentCart()
    }

    override fun addMenuToCart(menu: Menu) {
        orderViewModel.addMenuToCart(menu)
    }

    override fun rmMenuFromCart(menu: Menu) {
        orderViewModel.rmMenuFromCart(menu)
    }
}