package com.eseo.getmyspot.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.eseo.getmyspot.R
import com.eseo.getmyspot.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.net.InetAddress


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // <-- Référence à notre ViewBinding

    companion object {
        // récuperer le contexte pour changer d'activity
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // --> Indique que l'on utilise le ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
    }

    override fun onResume() {
        super.onResume()

        //On check si l'utilisateur à internet, dans le cas contraire on interdit l'utilisation de l'application
        val thread = Thread {
            if (!isInternetAvailable()) {
                runOnUiThread {
                    MaterialAlertDialogBuilder(this).setTitle(getString(R.string.require_internet_title))
                        .setCancelable(false)
                        .setMessage(R.string.require_internet_message)
                        .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                            finish()
                        }
                        .show()
                }
            }
        }

        thread.start()
    }

    fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            navHostFragment!!.navController
        )
    }

    fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr: InetAddress = InetAddress.getByName("google.com")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }
}

