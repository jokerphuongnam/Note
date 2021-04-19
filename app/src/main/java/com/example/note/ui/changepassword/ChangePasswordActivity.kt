package com.example.note.ui.changepassword

import androidx.activity.viewModels
import com.example.note.R
import com.example.note.databinding.ActivityChangePasswordBinding
import com.example.note.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity: BaseActivity<ActivityChangePasswordBinding, ChangePasswordViewModel>(R.layout.activity_change_password) {
    override fun createUI() {

    }

    override val viewModel: ChangePasswordViewModel by viewModels()
}