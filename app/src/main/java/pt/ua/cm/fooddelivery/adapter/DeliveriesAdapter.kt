package pt.ua.cm.fooddelivery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.databinding.DeliveryItemBinding
import pt.ua.cm.fooddelivery.network.response.DeliveriesResponse


class DeliveriesAdapter(
    private val deliveries: List<DeliveriesResponse>,
    //private val clickListener: MenuItemClickListener
): RecyclerView.Adapter<DeliveriesViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveriesViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = DeliveryItemBinding.inflate(from, parent, false)
        return DeliveriesViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: DeliveriesViewHolder, position: Int) {
        holder.bindDeliveryItem(deliveries[position])
    }

    override fun getItemCount(): Int = deliveries.size
}