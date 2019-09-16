package com.challenge.marleyspoon.features.recipeslist.main

import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.challenge.marleyspoon.R
import com.challenge.marleyspoon.base.BaseActivity

/**
 * @author moustafasamhoury
 * created on Thursday, 12 Sep, 2019
 */

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val appBarConfiguration = AppBarConfiguration(
        setOf(R.id.recipesListFragment)
    )

    override fun setupViews() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        setupActionBarWithNavController(
            navController = host.navController,
            configuration = appBarConfiguration
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }
}