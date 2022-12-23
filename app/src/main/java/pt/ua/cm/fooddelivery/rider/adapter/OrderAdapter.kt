package pt.ua.cm.fooddelivery.rider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.client.adapter.MenuItemClickListener
import pt.ua.cm.fooddelivery.client.entities.OrderWithMenus
import pt.ua.cm.fooddelivery.databinding.CartItemBinding
import pt.ua.cm.fooddelivery.databinding.OrderItemBinding
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import pt.ua.cm.fooddelivery.network.response.RiderOrderResponse
import timber.log.Timber


class OrderAdapter(
    private val orders: List<DeliveriesResponse>
): RecyclerView.Adapter<OrderViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = OrderItemBinding.inflate(from, parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bindOrderItem(orders[position])
    }

    override fun getItemCount(): Int = orders.size
}