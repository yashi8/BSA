package com.yashishu.bsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yashishu.bsa.databinding.OrderDetailRowBinding
import com.yashishu.bsa.models.Product

class OrderedProductAdapter(
    val onOrderClick: (Product) -> Unit
) : ListAdapter<Product, OrderedProductAdapter.ViewHolder>(OrderDiffUtil()) {

    class ViewHolder(val binding: OrderDetailRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(prd: Product) {
            binding.prd = prd
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = OrderDetailRowBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prd = getItem(position)
        holder.bind(prd)
        holder.itemView.setOnClickListener {
            onOrderClick(prd)
        }
    }

    class OrderDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

}