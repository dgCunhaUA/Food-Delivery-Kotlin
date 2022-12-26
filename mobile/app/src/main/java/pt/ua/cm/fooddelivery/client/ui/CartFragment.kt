package pt.ua.cm.fooddelivery.client.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.client.adapter.MenuItemClickListener
import pt.ua.cm.fooddelivery.client.entities.Menu
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.client.viewmodel.CartModelFactory
import pt.ua.cm.fooddelivery.client.viewmodel.CartViewModel
import pt.ua.cm.fooddelivery.databinding.FragmentCartBinding
import timber.log.Timber


class CartFragment : Fragment(), MenuItemClickListener {

    private lateinit var binding: FragmentCartBinding

    private val cartViewModel: CartViewModel by viewModels {
        CartModelFactory((activity?.application as DeliveryApplication).orderRepository,
            (activity?.application as DeliveryApplication).userRepository,
            (activity?.application as DeliveryApplication).restaurantRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView")

        binding = FragmentCartBinding.inflate(layoutInflater)

        setFields()

        binding.finishOrderBtn.setOnClickListener {
            cartViewModel.finishOrder()
        }

        return binding.root
    }

    private fun setFields()
    {
        val mainActivity = this.activity
        if (mainActivity != null) {
            cartViewModel.currentCart.observe(viewLifecycleOwner) {
                Timber.i("Cart Observer! $it")
                binding.cartRecyclerView.apply {
                    if(it != null) {
                        layoutManager = LinearLayoutManager(activity?.applicationContext)
                        adapter =
                            pt.ua.cm.fooddelivery.client.adapter.CartAdapter(it, this@CartFragment)
                    }
                }

                if(it != null) {
                    if(it.menus.isNotEmpty()) {
                        binding.finishOrderBtn.visibility = View.VISIBLE
                    } else {
                        binding.finishOrderBtn.visibility = View.GONE
                    }
                }
            }
            cartViewModel.getCurrentCart()

            cartViewModel.cartResult.observe(viewLifecycleOwner) {
                Timber.i("Cart results observer: $it")
                when (it) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }
                    is BaseResponse.Success -> {
                        stopLoading()
                    }
                    is BaseResponse.Error -> {
                        stopLoading()
                        processError(it.msg)
                    }
                    else -> {
                        stopLoading()
                    }
                }
            }

            binding.deliveriesStatusBtn.setOnClickListener {
                Timber.i("Navigating to Deliveries Fragment")
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_cart_to_deliveries_fragment)
            }
        }
    }

    override fun addMenuToCart(menu: Menu) {
        cartViewModel.addMenuToCart(menu)
    }

    override fun rmMenuFromCart(menu: Menu) {
        cartViewModel.rmMenuFromCart(menu)
    }


    private fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

    private fun processError(msg: String?) {
        showToast("Error: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}