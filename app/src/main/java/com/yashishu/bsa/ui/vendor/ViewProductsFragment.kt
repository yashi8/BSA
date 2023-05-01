package com.yashishu.bsa.ui.vendor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentViewProductsBinding

class ViewProductsFragment: Fragment() {
    private val viewModel:VendorDashboardViewModel by activityViewModels()
    private lateinit var _binding: FragmentViewProductsBinding
    private val binding get() = _binding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_products, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetProductState()
        binding.editproducts.setOnClickListener {
            findNavController().navigate(R.id.action_viewProductsFragment_to_editProductFragment)
        }
        binding.ivmore.setOnClickListener {
            val menu = PopupMenu(requireContext(), it)
            menu.inflate(R.menu.product_popup_menu)
            menu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_delete -> {
                        viewModel.deleteProduct(db)
                        findNavController().navigateUp()
                        true
                    }
                 /*
                 R.id.action_report -> {
                        true
                  }


                    R.id.action_rationgs -> {
                        true
                    }*/
                    else -> false
                }
            }
            menu.show()
        }
    }
    }




