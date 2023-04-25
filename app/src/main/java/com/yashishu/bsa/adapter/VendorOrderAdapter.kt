package com.yashishu.bsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yashishu.bsa.databinding.OrderRowBinding
import com.yashishu.bsa.models.Order

class VendorOrderAdapter(
    val onOrderClick: (Order) -> Unit
) : ListAdapter<Order, VendorOrderAdapter.ViewHolder>(VendorOrderAdapter.OrderDiffUtil()) {

    class ViewHolder(val binding: OrderRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.order = order
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = OrderRowBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
        holder.binding.btnViewOrder.setOnClickListener {
            onOrderClick(order)
        }
    }

    class OrderDiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

}