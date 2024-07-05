package com.zalomsky.rickandmorty

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zalomsky.rickandmorty.databinding.ActivityMainBinding
import com.zalomsky.rickandmorty.features.characters.CharactersFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
            if (fragment is CharactersFragment) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }

        val bottomNavigationView = binding.bottomNavView
        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsLocationFragment -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}