package com.example.weatherforecast

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherforecast.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private val mainNavController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.am_nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLoadingView(am_loading_container_fl)
    }
}