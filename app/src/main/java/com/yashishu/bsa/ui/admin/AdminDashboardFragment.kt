package com.yashishu.bsa.ui.admin

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.AuthActivity
import com.yashishu.bsa.R

class AdminDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = AdminDashboardFragment()
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: AdminDashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false)
    }


    fun logout() {
        auth.signOut()
        requireActivity().startActivity(Intent(requireContext(), AuthActivity::class.java))
    }

}