package pt.ua.cm.fooddelivery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.adapter.CartAdapter
import pt.ua.cm.fooddelivery.cart.*
import pt.ua.cm.fooddelivery.databinding.FragmentCartBinding
import pt.ua.cm.fooddelivery.entities.Menu
import pt.ua.cm.fooddelivery.adapter.MenuItemClickListener
import timber.log.Timber


class CartFragment : Fragment(), MenuItemClickListener {

    private lateinit var binding: FragmentCartBinding

    private val orderViewModel: OrderViewModel by viewModels {
        OrderModelFactory((activity?.application as DeliveryApplication).orderRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView")

        binding = FragmentCartBinding.inflate(layoutInflater)

        setFields()
        observeFeedback()

        return binding.root
    }

    private fun setFields()
    {
        val mainActivity = this.activity
        if (mainActivity != null) {
            orderViewModel.currentCart.observe(viewLifecycleOwner) {
                Timber.i("Cart Observer!")
                binding.cartRecyclerView.apply {
                    if(it != null) {
                        layoutManager = LinearLayoutManager(activity?.applicationContext)
                        adapter = CartAdapter(it, this@CartFragment)
                    }
                }
            }
            orderViewModel.getCurrentCart()
        }
    }

    private fun observeFeedback() {
        orderViewModel.feedbackMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                //val toast = Toast.makeText(context, it, Toast.LENGTH_SHORT)
                //toast.show()
            }
            //orderViewModel.feedbackMessage.postValue(null)
        }
        orderViewModel.getCurrentCart()
    }

    override fun addMenuToCart(menu: Menu) {
        orderViewModel.addMenuToCart(menu)
        //orderViewModel.getCurrentCart()
    }

    override fun rmMenuFromCart(menu: Menu) {
        orderViewModel.rmMenuFromCart(menu)
        //orderViewModel.getCurrentCart()
    }
}