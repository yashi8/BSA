package com.yashishu.bsa.ui.vendor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentVendorDashboardBinding

class VendorDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = VendorDashboardFragment()
    }

    private lateinit var viewModel: VendorDashboardViewModel
    private var _binding: FragmentVendorDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVendorDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            addpro.setOnClickListener { gotoAddProductScreen() }
            btnlogout.setOnClickListener {
                auth.signOut()
                startActivity()

            }
        }
    }

    private fun gotoAddProductScreen() {
        findNavController().navigate(R.id.action_vendorDashboardFragment_to_addProductFragment)
    }

}