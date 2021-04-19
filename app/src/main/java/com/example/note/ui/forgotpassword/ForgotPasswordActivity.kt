package com.example.note.ui.forgotpassword

import androidx.activity.viewModels
import com.example.note.R
import com.example.note.databinding.ActivityForgotPassowdBinding
import com.example.note.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity: BaseActivity<ActivityForgotPassowdBinding, ForgotPasswordViewModel>(R.layout.activity_forgot_passowd){
    override fun createUI() {

    }

    override val viewModel: ForgotPasswordViewModel by viewModels()
}