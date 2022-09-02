package com.bbj.kinono.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bbj.kinono.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(),NavigateInterface {

    val viewModel by viewModel<MainViewModel>()

    val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.main_fragment_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolBar: Toolbar = findViewById(R.id.custom_toolbar)

        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


    }

    override fun navigateFromMainToMovieFragment(bundle: Bundle) {
        navController.navigate(R.id.action_mainFragment_to_movieFragment, bundle)
    }

    override fun navigateFromMainToSeeMoreMovieFragment() {
        navController.navigate(R.id.action_mainFragment_to_seeMoreMovieFragment)
    }

    override fun navigateFromMainToSeeMoreCastFragment() {
        navController.navigate(R.id.action_movieFragment_to_seeMoreCastFragment)
    }

    override fun navigateFromSeeMoreToMovieFragment(bundle: Bundle) {
        navController.navigate(R.id.action_seeMoreMovieFragment_to_movieFragment,bundle)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}