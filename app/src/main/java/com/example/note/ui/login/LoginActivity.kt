package com.example.note.ui.login

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.example.note.R
import com.example.note.databinding.ActivityLoginBinding
import com.example.note.ui.base.BaseActivity
import com.example.note.ui.main.MainActivity
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

        }
    }
    private val forgotPasswordClick: View.OnClickListener by lazy {
        View.OnClickListener {

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

                }
                is Resource.Success -> {
                    val data: Intent = Intent(this, MainActivity::class.java).apply {
                        putExtra(UID,resource.data?.uid)
                    }
                    startActivity(data)
                }
                is Resource.Error -> {

                }
            }
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

    companion object{
        const val UID: String = "uid"
    }
}