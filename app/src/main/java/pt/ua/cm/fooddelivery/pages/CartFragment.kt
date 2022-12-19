package pt.ua.cm.fooddelivery.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.cart.*
import pt.ua.cm.fooddelivery.databinding.FragmentCartBinding
import timber.log.Timber

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private val cartViewModel: OrderViewModel by viewModels {
        OrderModelFactory((activity?.application as DeliveryApplication).orderRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView")

        binding = FragmentCartBinding.inflate(layoutInflater)

        setFields()

        return binding.root
    }

    private fun setFields()
    {
        val mainActivity = this.activity
        if (mainActivity != null) {
            cartViewModel.cartMenus.observe(viewLifecycleOwner) {

                binding.cartRecyclerView.apply {
                    if(it != null) {
                        layoutManager = LinearLayoutManager(activity?.applicationContext)
                        adapter = CartAdapter(it)
                    }
                }
            }
            cartViewModel.getCartMenus()
        }
    }
}