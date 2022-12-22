package pt.ua.cm.fooddelivery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.adapter.CartAdapter
import pt.ua.cm.fooddelivery.databinding.FragmentCartBinding
import pt.ua.cm.fooddelivery.entities.Menu
import pt.ua.cm.fooddelivery.adapter.MenuItemClickListener
import pt.ua.cm.fooddelivery.entities.OrderWithMenus
import pt.ua.cm.fooddelivery.network.response.BaseResponse
import pt.ua.cm.fooddelivery.viewmodel.CartModelFactory
import pt.ua.cm.fooddelivery.viewmodel.CartViewModel
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
        observeFeedback()

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
                        adapter = CartAdapter(it, this@CartFragment)
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

        }
    }

    private fun observeFeedback() {
        cartViewModel.feedbackMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                //val toast = Toast.makeText(context, it, Toast.LENGTH_SHORT)
                //toast.show()
            }
            //orderViewModel.feedbackMessage.postValue(null)
        }
        cartViewModel.getCurrentCart()
    }

    override fun addMenuToCart(menu: Menu) {
        cartViewModel.addMenuToCart(menu)
        //orderViewModel.getCurrentCart()
    }

    override fun rmMenuFromCart(menu: Menu) {
        cartViewModel.rmMenuFromCart(menu)
        //orderViewModel.getCurrentCart()
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