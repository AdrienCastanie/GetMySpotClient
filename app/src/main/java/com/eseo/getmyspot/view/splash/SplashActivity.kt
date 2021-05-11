package com.eseo.getmyspot.view.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.preferences.LocalPreferences
import com.eseo.getmyspot.databinding.ActivitySplashBinding
import com.eseo.getmyspot.view.main.MainActivity
import java.util.*

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding // <-- Référence à notre ViewBinding

    companion object {
        // récuperer le contexte pour changer d'activity
        fun getStartIntent(context: Context) : Intent {
            val intent = Intent(context, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get language
        var language = LocalPreferences.getInstance(this).getStringStringValue("language")
        if (language == null) {
            // if not set, set to default language
            language = Locale.getDefault().getLanguage()
            LocalPreferences.getInstance(this).saveStringValue("language", language)
        }
        // set language for the app
        setLocale(this, language)

        // splash screen during 2sec after start main activity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(MainActivity.getStartIntent(this))
            finish()
        }, 2000)
    }

    // set locale language
    fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.getConfiguration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.getDisplayMetrics())
    }
}
    