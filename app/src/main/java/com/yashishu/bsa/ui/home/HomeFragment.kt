package com.yashishu.bsa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yashishu.bsa.PrefUtil
import com.yashishu.bsa.R
import com.yashishu.bsa.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (PrefUtil(requireActivity()).getUserType()){
            0 -> findNavController().navigate(R.id.action_navigation_home_to_adminDashboardFragment )
            1 -> findNavController().navigate(R.id.action_navigation_home_to_vendorDashboardFragment )
            2 -> findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}