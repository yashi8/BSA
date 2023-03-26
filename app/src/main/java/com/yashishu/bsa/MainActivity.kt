package com.yashishu.bsa

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yashishu.bsa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userType = PrefUtil(this).getUserType()
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        when (userType) {
            0 -> {
                navView.menu.clear()
                navView.inflateMenu(R.menu.admin_bottom_nav_menu)
                navController.setGraph(R.navigation.admin_navigation)
                val appBarConfiguration = AppBarConfiguration(setOf(R.id.admin_nav_dashboard))
                setupActionBarWithNavController(navController, appBarConfiguration)
            }
            1 -> {
                navView.menu.clear()
                navView.inflateMenu(R.menu.vendor_bottom_nav_menu)
                navController.setGraph(R.navigation.vendor_navigation)
                val appBarConfiguration = AppBarConfiguration(setOf(R.id.vendor_nav_dashboard, R.id.vendor_nav_product, R.id.vendor_nav_orders,R.id.Vendor_nav_Account))
                setupActionBarWithNavController(navController, appBarConfiguration)
            }
            2 -> {
                navView.menu.clear()
                navView.inflateMenu(R.menu.user_bottom_nav_menu)
                navController.setGraph(R.navigation.user_navigation)
                val appBarConfiguration = AppBarConfiguration(setOf(R.id.user_nav_home, R.id.user_nav_dashboard,R.id.user_nav_Cart,R.id.user_nav_Account))
                setupActionBarWithNavController(navController, appBarConfiguration)
            }
        }



        navView.setupWithNavController(navController)
    }

    //menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            auth.signOut()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return true
        }


        return false
    }
}