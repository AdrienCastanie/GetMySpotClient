package com.eseo.getmyspot.view.connection

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eseo.getmyspot.databinding.ActivityConnectionBinding
import com.eseo.getmyspot.databinding.ActivityMainBinding
import com.eseo.getmyspot.view.main.MainActivity

class ConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConnectionBinding

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ConnectionActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}