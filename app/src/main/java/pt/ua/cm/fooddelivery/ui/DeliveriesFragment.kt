package pt.ua.cm.fooddelivery.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.FragmentCartBinding
import pt.ua.cm.fooddelivery.databinding.FragmentDeliveriesBinding
import pt.ua.cm.fooddelivery.viewmodel.DeliveriesModelFactory
import pt.ua.cm.fooddelivery.viewmodel.DeliveriesViewModel

class DeliveriesFragment : Fragment() {

    private lateinit var binding: FragmentDeliveriesBinding

    private val deliveriesViewModel: DeliveriesViewModel by viewModels {
        DeliveriesModelFactory((activity?.application as DeliveryApplication).orderRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_deliveries, container, false)
    }

}