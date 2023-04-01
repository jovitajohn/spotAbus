package com.jovita.spotabus

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.ArCoreApk
import com.jovita.spotabus.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        if(maybeArOrQr()) {
        // change view
            navigateInHome(true)
        }else{
            navigateInHome(false)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun navigateInHome(isAr: Boolean) {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        if(isAr) {
            navController.navigate(R.id.nav_gallery)
        }else {
            navController.navigate(R.id.nav_home)
        }
    }

    fun maybeArOrQr() : Boolean{

        val availability = ArCoreApk.getInstance().checkAvailability(this)
       /* if (availability.isTransient) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            Handler().postDelayed({
                maybeArOrQr()
            }, 200)
        }*/
        return if(availability.name == "SUPPORTED_INSTALLED" || availability.name == "SUPPORTED_NOT_INSTALLED"){
            availability.isSupported
        }else{
            false
        }


    }
}