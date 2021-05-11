package com.eseo.getmyspot.view.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.preferences.LocalPreferences
import com.eseo.getmyspot.databinding.ActivitySettingsBinding
import com.eseo.getmyspot.view.splash.SplashActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding // <-- Référence à notre ViewBinding

    companion object {
        // récuperer le contexte pour changer d'activity
        fun getStartIntent(context: Context) : Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setTitle(R.string.settings)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.toggleButtonDarkTheme.setChecked(true)
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.toggleButtonDarkTheme.setChecked(false)
            }
        }


        binding.toggleButtonDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
                LocalPreferences.getInstance(this).setThemeValue(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
                LocalPreferences.getInstance(this).setThemeValue(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        // get language via LocalPreferences and set the appropriate flag
        if (LocalPreferences.getInstance(this).getStringStringValue("language") == "en") {
            binding.language.setImageResource(R.drawable.angleterre)
        } else {
            binding.language.setImageResource(R.drawable.france)
        }

        // action au clic sur le bouton language
        binding.language.setOnClickListener {
            // get language from LocalPreferences
            var language = LocalPreferences.getInstance(this).getStringStringValue("language")
            // if english, set to french and set french flag
            if (language == "en") {
                language = "fr"
            } else { // if it's an other language set to english and set english flag
                language = "en"
            }
            // set localpreferences with the new language
            LocalPreferences.getInstance(this).saveStringValue("language", language)
            // restart app
            startActivity(SplashActivity.getStartIntent(this))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}