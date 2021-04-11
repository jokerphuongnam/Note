package com.example.note.ui.login

import androidx.activity.viewModels
import com.example.note.R
import com.example.note.databinding.ActivityLoginBinding
import com.example.note.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    override val viewModel: LoginViewModel by viewModels()

    override fun action() {

    }

    override fun onBackPressed() {
        if (isEmptyFragmentBackStack) {
            twiceTimeToExit()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}