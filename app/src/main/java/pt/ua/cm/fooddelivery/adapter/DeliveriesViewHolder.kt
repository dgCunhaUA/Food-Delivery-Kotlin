package pt.ua.cm.fooddelivery.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
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

        binding.showMapBtn.setOnClickListener {
            Timber.i("Navigate to map of: $order")
            //clickListener.addMenuToCart(menu)
        }
    }
}