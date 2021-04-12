package com.example.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.instagramclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    //mendeklarasikan appimport kotlinx.android.synthetic.main.activity_main.*BarConfiguration untk Bottom Navigation
    private lateinit var appBarConfig : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var inflater = layoutInflater
        binding = ActivityMainBinding.inflate(inflater)

        setContentView(binding.root)

        val host: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment? ?:return
        val navController= host!!.navController
        binding.navBar.setupWithNavController(navController)
    }
}