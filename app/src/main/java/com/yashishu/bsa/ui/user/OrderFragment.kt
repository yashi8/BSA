package com.yashishu.bsa.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.OrderCardLayoutBinding
import com.yashishu.bsa.models.Order


class OrderFragment : Fragment() {

    private var _binding: com.yashishu.bsa.databinding.FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    private val viewModel: ProductViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getOrders(db, auth)

        viewModel.orders.observe(viewLifecycleOwner) {
            val orderAdapter = OrderAdapter {
                viewModel.setSelectedOrder(it)
            }
            binding.rvUserOrders.layoutManager = LinearLayoutManager(requireContext())
            binding.rvUserOrders.adapter = orderAdapter
            orderAdapter.submitList(it)

        }
    }

    class OrderAdapter(
        private val listener: (Order) -> Unit
    ) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiffUtil()) {
        class OrderViewHolder(private val binding: OrderCardLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(order: Order, listener: (Order) -> Unit) {
                binding.order = order
                binding.root.setOnClickListener {
                    listener(order)
                }
            }

            companion object {
                fun from(parent: ViewGroup): OrderViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = OrderCardLayoutBinding.inflate(layoutInflater, parent, false)
                    return OrderViewHolder(binding)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            return OrderViewHolder.from(parent)
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            holder.bind(getItem(position), listener)
        }

        class OrderDiffUtil : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.date == newItem.date
            }
        }
    }
}