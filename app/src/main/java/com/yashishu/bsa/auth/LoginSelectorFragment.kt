package com.yashishu.bsa.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentLoginSelectorBinding

class LoginSelectorFragment : Fragment() {

    private var _binding: FragmentLoginSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            adminlogin.setOnClickListener { gotoAdminScreen() }
            userlogin.setOnClickListener { gotoCustomerScreen() }
            vendorlogin.setOnClickListener { gotoVendorScreen() }
        }
    }

    private fun gotoVendorScreen() {
        findNavController().navigate(R.id.action_loginSelectorFragment_to_vndrLoginFragment)
    }

    private fun gotoCustomerScreen() {
        findNavController().navigate(R.id.action_loginSelectorFragment_to_custLoginFragment)
    }

    private fun gotoAdminScreen() {
        findNavController().navigate(R.id.action_loginSelectorFragment_to_admLoginFragment)
    }
}