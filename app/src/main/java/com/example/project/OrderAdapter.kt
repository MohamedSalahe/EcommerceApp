package com.example.project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.OrderListBinding
import com.example.project.model.OrderItems


class OrderAdapter (private val onDelete: (String) -> Unit,private val onDetails: (OrderItems) -> Unit) : ListAdapter<OrderItems, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {
    inner class OrderViewHolder(private val binding: OrderListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItems) {
            binding.orderNumber.text = "Order No #${item.orderID}"
            binding.orderDate.text = item.date
            binding.orderCount.text = "${item.productCount} items"
            binding.orderPrice.text = "${item.price}$"
            binding.btnCancel.setOnClickListener {
                onDelete(item.orderID)
            }
            binding.btnViewDetails.setOnClickListener {
                onDetails(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class OrderDiffCallback : DiffUtil.ItemCallback<OrderItems>() {
    override fun areItemsTheSame(oldItem: OrderItems, newItem: OrderItems): Boolean {
        return oldItem.orderID == newItem.orderID
    }
    override fun areContentsTheSame(oldItem: OrderItems, newItem: OrderItems): Boolean {
        return oldItem == newItem
    }
}

