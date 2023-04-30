package com.yashishu.bsa.ui.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.databinding.FragmentUserOrderDetailBinding
import com.yashishu.bsa.databinding.UserOrderDetailLayoutBinding
import com.yashishu.bsa.models.CartItem
import com.yashishu.bsa.models.Product

class UserOrderDetailFragment : Fragment() {

    private val viewModel: ProductViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentUserOrderDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UserOrderDetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserOrderDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.selectedOrder.value != null) {
            val adapter = UserOrderDetailAdapter {
                val mapIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=${it.lat},${it.lng}")
                )
                startActivity(mapIntent)
            }
            binding.rvUserOrderDetail.adapter = adapter
            binding.rvUserOrderDetail.layoutManager = LinearLayoutManager(requireContext())
            adapter.submitList(viewModel.selectedOrder.value!!.items)
        }
    }

    class UserOrderDetailAdapter(val onClick: (Product) -> Unit) :
        ListAdapter<CartItem, UserOrderDetailAdapter.UserOrderDetailViewHolder>(DiffCallback) {
        class UserOrderDetailViewHolder(private val binding: UserOrderDetailLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(item: CartItem, onClick: (Product) -> Unit) {
                binding.item = item
                binding.fabLocate.setOnClickListener {
                    onClick(item.product)
                }
                binding.executePendingBindings()
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): UserOrderDetailViewHolder {
            val binding = UserOrderDetailLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return UserOrderDetailViewHolder(binding)
        }

        override fun onBindViewHolder(holder: UserOrderDetailViewHolder, position: Int) {
            val item = getItem(position)
            holder.bind(item, onClick)
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.product.title == newItem.product.title
            }

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
