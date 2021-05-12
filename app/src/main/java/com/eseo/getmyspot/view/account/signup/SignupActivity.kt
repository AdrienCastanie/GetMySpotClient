package com.eseo.getmyspot.view.account.signup

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.eseo.getmyspot.R
import com.eseo.getmyspot.databinding.ActivitySigninBinding
import com.eseo.getmyspot.databinding.ActivitySignupBinding
import com.eseo.getmyspot.view.account.signin.SigninActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SignupActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtInputPasswordConfirmation.addTextChangedListener {
            if(it.toString() == binding.txtInputPassword.text.toString())
                binding.txtInputPasswordConfirmation.setTextColor(getColor(R.color.green_validate))
            else
                binding.txtInputPasswordConfirmation.setTextColor(Color.RED)
        }
    }
}