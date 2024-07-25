package com.example.mediforme

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mediforme.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // setContentView를 먼저 호출합니다.

        setNavigation() // 이후에 setNavigation 호출
    }

    private fun setNavigation() {
        binding.mainBnv.itemIconTintList = null

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainNaviFragmentContainer) as? NavHostFragment
            ?: return

        navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)

        // 최소 실행시 프래그먼트 설정
        binding.mainBnv.selectedItemId = R.id.navigation_home
        navController.navigate(R.id.navigation_home)
    }
}
