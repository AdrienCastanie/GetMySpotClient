package com.eseo.getmyspot.view.account.signin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.preferences.LocalPreferences
import com.eseo.getmyspot.databinding.ActivitySigninBinding
import com.eseo.getmyspot.view.Failed
import com.eseo.getmyspot.view.account.signup.SignupActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response.error

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private val signInViewModel: SignInViewModel by viewModel()

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SigninActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
    }

    private fun setupUi() {
        signInViewModel.states.observe(this, Observer { state ->
            when (state) {
                //is Loading -> TODO : peut être mettre un logo de chargement plus tard
                is SignInViewModel.CallResult ->
                    if (state.isConnectToAccount) {
                        LocalPreferences.getInstance(this).saveStringValue(LocalPreferences.PSEUDO, binding.txtInputPseudo.text.toString())
                        finish()
                    } else {
                        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.connection_error_title))
                            .setMessage(getString(R.string.connection_error_message))
                            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                            }
                            .show()
                    }
                is Failed -> Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnCreateAccount.setOnClickListener {
            startActivity(SignupActivity.getStartIntent(this))
        }
        binding.btnLogIn.setOnClickListener {
            signInViewModel.doRemoteAction(binding.txtInputPseudo.text.toString(), binding.txtInputPassword.text.toString())
        }
    }
}