package com.eseo.getmyspot.view.account.signup

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.eseo.getmyspot.R
import com.eseo.getmyspot.databinding.ActivitySigninBinding
import com.eseo.getmyspot.databinding.ActivitySignupBinding
import com.eseo.getmyspot.view.Failed
import com.eseo.getmyspot.view.Loading
import com.eseo.getmyspot.view.account.signin.SigninActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val createAccountViewModel: CreateAccountViewModel by viewModel()
    private var passwordValidation: Boolean = false

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SignupActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
    }

    private fun setupUi() {
        createAccountViewModel.states.observe(this, Observer { state ->
            when (state) {
                is Loading -> Toast.makeText(this, "loading..", Toast.LENGTH_SHORT).show()
                is CreateAccountViewModel.CallResult -> Toast.makeText(
                    this,
                    "FINISH : " + state.isAccountCreate,
                    Toast.LENGTH_SHORT
                ).show()
                is Failed -> Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        binding.txtInputPasswordConfirmation.addTextChangedListener {
            if (it.toString() == binding.txtInputPassword.text.toString()) {
                binding.txtInputPasswordConfirmation.setTextColor(getColor(R.color.green_validate))
                passwordValidation = true
            } else {
                binding.txtInputPasswordConfirmation.setTextColor(Color.RED)
                passwordValidation = false
            }
        }
        binding.btnCreateAccount.setOnClickListener {
            if(passwordValidation)
                createAccountViewModel.doRemoteTestAction(binding.txtInputPseudo.text.toString(), binding.txtInputPassword.text.toString())
            else
                Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show()
        }
    }
}