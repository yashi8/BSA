package com.yashishu.bsa.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.yashishu.bsa.R
import com.yashishu.bsa.adapter.CartAdapter
import com.yashishu.bsa.databinding.FragmentUsercartBinding

class UserCartFragment : Fragment() {

    private var _binding: FragmentUsercartBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private val viewModel: ProductViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_usercart, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCartItems(db, auth)
        val adapter = CartAdapter {
            viewModel.removeCartItem(db, auth, it)
        }
        binding.apply {
            rvCart.layoutManager = LinearLayoutManager(requireContext())
            rvCart.adapter = adapter
        }
        viewModel.cartItems.observe(viewLifecycleOwner) {
            binding.apply {
                if (it.isEmpty()) {
                    tvEmptyCart.visibility = View.VISIBLE
                    rvCart.visibility = View.GONE
                    fabPay.visibility = View.GONE
                } else {
                    tvEmptyCart.visibility = View.GONE
                    rvCart.visibility = View.VISIBLE
                    fabPay.visibility = View.VISIBLE
                    adapter.submitList(it)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}