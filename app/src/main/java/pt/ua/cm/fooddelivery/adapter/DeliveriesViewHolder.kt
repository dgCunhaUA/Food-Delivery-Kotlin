package pt.ua.cm.fooddelivery.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.DeliveryItemBinding
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import timber.log.Timber

class DeliveriesViewHolder(
    private val context: Context,
    private val binding: DeliveryItemBinding,
    //private val clickListener: DeliverItemClickListener
): RecyclerView.ViewHolder(binding.root)
{

    fun bindDeliveryItem(order: DeliveriesResponse)
    {
        binding.deliveryName.text = order.order_status

        if(order.order_status == "Delivering") {
            binding.showMapBtn.visibility = View.VISIBLE
            binding.showMapBtn.setOnClickListener {
                Timber.i("Navigate to map of: $order")

                var riderLocation: LatLng? = null
                if (order.rider_lat != null && order.rider_lng != null) {
                    riderLocation = LatLng(order.rider_lat!!, order.rider_lng!!)
                }

                val bundle = bundleOf("riderLocation" to riderLocation)
                it.findNavController()
                    .navigate(R.id.action_deliveries_fragment_to_mapsFragment, bundle)
            }
        } else {
            binding.showMapBtn.visibility = View.GONE
        }
    }
}