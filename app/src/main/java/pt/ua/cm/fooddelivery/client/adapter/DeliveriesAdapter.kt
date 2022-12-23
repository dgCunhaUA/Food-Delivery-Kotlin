package pt.ua.cm.fooddelivery.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse
import pt.ua.cm.fooddelivery.databinding.DeliveryItemBinding


class DeliveriesAdapter(
    private val deliveries: List<DeliveriesResponse>,
    //private val clickListener: MenuItemClickListener
): RecyclerView.Adapter<pt.ua.cm.fooddelivery.client.adapter.DeliveriesViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pt.ua.cm.fooddelivery.client.adapter.DeliveriesViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = DeliveryItemBinding.inflate(from, parent, false)
        return pt.ua.cm.fooddelivery.client.adapter.DeliveriesViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: pt.ua.cm.fooddelivery.client.adapter.DeliveriesViewHolder, position: Int) {
        holder.bindDeliveryItem(deliveries[position])
    }

    override fun getItemCount(): Int = deliveries.size
}