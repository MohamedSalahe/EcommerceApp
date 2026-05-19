package com.example.project


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.OrderDetailsBinding
import com.example.project.model.OrderDetails2
import android.view.LayoutInflater
import android.view.ViewGroup


class OrderDetailsAdapter : ListAdapter<OrderDetails2, OrderDetailsAdapter.ProductViewHolder>(OrderDetailsDiffCallback()) {
    inner class ProductViewHolder(private val binding: OrderDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderDetails2) {
            binding.ProductName.text = item.orderItem
            binding.Quantity.text    = "x${item.itemCount}"
            binding.Price.text       = "$${item.itemPrice}"
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = OrderDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class OrderDetailsDiffCallback : DiffUtil.ItemCallback<OrderDetails2>() {
    override fun areItemsTheSame(oldItem: OrderDetails2, newItem: OrderDetails2): Boolean {
        return oldItem.productId == newItem.productId
    }
    override fun areContentsTheSame(oldItem: OrderDetails2, newItem: OrderDetails2): Boolean {
        return oldItem == newItem
    }
}


