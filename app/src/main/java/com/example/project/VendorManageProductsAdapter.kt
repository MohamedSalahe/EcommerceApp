package com.example.project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.databinding.ItemProductBinding
import com.example.project.model.ProductItem


class ProductAdapter (private val onDelete: (String) -> Unit,private val onDetails: (ProductItem) -> Unit) : ListAdapter<ProductItem, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {
    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductItem) {
            binding.productName.text = item.name
            binding.stockView.text = "${item.stock} in stock"
            binding.ratingView.text = item.rating
            if (item.image.isNotEmpty()) {
                Glide.with(binding.productImage.context)
                    .load(item.image)
                    .into(binding.productImage)
            }
            binding.btnRemove.setOnClickListener {
                onDelete(item.productID)
            }
            binding.btnViewDetails.setOnClickListener {
                onDetails(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem.productID == newItem.productID
        }
        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem == newItem
        }
    }

