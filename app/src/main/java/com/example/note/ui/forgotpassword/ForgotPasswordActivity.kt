package com.example.note.ui.forgotpassword

import android.view.View
import androidx.activity.viewModels
import com.example.note.R
import com.example.note.databinding.ActivityForgotPassowdBinding
import com.example.note.ui.base.BaseActivity
import com.example.note.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity: BaseActivity<ActivityForgotPassowdBinding, ForgotPasswordViewModel>(R.layout.activity_forgot_passowd){
    override fun createUI() {
        binding.apply {
            recoverPasswordBtn.setOnClickListener {
                viewModel.recoverPassword(username.editText!!.text.toString())
            }
        }
        viewModel.changePasswordLiveData.observe{resource ->
            when(resource){
                is Resource.Loading -> {
                    binding.recoverPasswordError.visibility = View.INVISIBLE
                }
                is Resource.Success -> {
                    finish()
                    showToast(getString(R.string.recover_password_success))
                }
                is Resource.Error -> {
                    binding.recoverPasswordError.visibility = View.VISIBLE
                    binding.recoverPasswordError.text = resource.message
                }
            }
        }
        viewModel.internetError.observe{
            binding.recoverPasswordError.visibility = View.VISIBLE
            binding.recoverPasswordError.setText(R.string.no_internet)
        }
    }

    override val viewModel: ForgotPasswordViewModel by viewModels()
}