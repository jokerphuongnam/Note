package com.example.note.ui.login

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.example.note.R
import com.example.note.databinding.ActivityLoginBinding
import com.example.note.ui.base.BaseActivity
import com.example.note.ui.forgotpassword.ForgotPasswordActivity
import com.example.note.ui.main.MainActivity
import com.example.note.ui.register.RegisterActivity
import com.example.note.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    private val loginClick: View.OnClickListener by lazy {
        View.OnClickListener {
            viewModel.loginEmailPass(
                binding.username.editText?.text!!.toString(),
                binding.password.editText?.text!!.toString()
            )
        }
    }
    private val registerClick: View.OnClickListener by lazy {
        View.OnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    private val forgotPasswordClick: View.OnClickListener by lazy {
        View.OnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    override fun createUI() {
        noInternetError()
        binding.apply {
            login.setOnClickListener(loginClick)
            register.setOnClickListener(registerClick)
            forgotPassword.setOnClickListener(forgotPasswordClick)
        }
        viewModel.login.observe { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.loginError.visibility = View.INVISIBLE
                }
                is Resource.Success -> {
                    val data: Intent = Intent(this, MainActivity::class.java).apply {
                        putExtra(UID, resource.data?.uid)
                    }
                    startActivity(data)
                }
                is Resource.Error -> {
                    binding.loginError.visibility = View.VISIBLE
                    binding.loginError.setText(R.string.wrong_password)
                }
            }
        }
        viewModel.internetError.observe {
            binding.loginError.visibility = View.VISIBLE
            binding.loginError.setText(R.string.no_internet)
        }
    }

    override fun onBackPressed() {
        if (isEmptyFragmentBackStack) {
            twiceTimeToExit()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override val viewModel: LoginViewModel by viewModels()

    companion object {
        const val UID: String = "uid"
    }
}