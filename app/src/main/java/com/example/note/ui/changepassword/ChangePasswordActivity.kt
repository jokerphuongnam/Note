package com.example.note.ui.changepassword

import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.example.note.R
import com.example.note.databinding.ActivityChangePasswordBinding
import com.example.note.throwable.NoErrorException
import com.example.note.ui.base.BaseActivity
import com.example.note.utils.InputUtils
import com.example.note.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity :
    BaseActivity<ActivityChangePasswordBinding, ChangePasswordViewModel>(R.layout.activity_change_password) {

    private lateinit var actionBar: ActionBar

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbar)
        actionBar = supportActionBar!!
    }

    /**
     * after click change password will check repeat password with new password will set error for repeat password
     * if repeat password do not same new password
     * else check password in net work
     * */
    private fun setUpEvent() {
        binding.apply {
            changePasswordBtn.setOnClickListener {
                if (!repeatPassword.editText!!.text.toString()
                        .equals(newPassword.editText!!.text.toString())
                ) {
                    repeatPassword.error = getString(R.string.repeat_password_other_new_password)
                } else {
                    viewModel.checkOldPassword(oldPassword.editText!!.text.toString())
                }
            }
        }
    }

    private fun setUpObserver() {
        viewModel.apply {
            checkPasswordLiveData.observe { resource ->
                binding.apply {
                    when (resource) {
                        /**
                         * first time hide all error
                         * */
                        is Resource.Loading -> {
                            changePasswordError.visibility = View.INVISIBLE
                            oldPassword.error = null
                            newPassword.error = null
                            repeatPassword.error = null
                        }
                        /**
                         * passwordRegex will throw exception if don't have error
                         * else request change password in network
                         * */
                        is Resource.Success -> {
                            try {
                                newPassword.error =
                                    getString(InputUtils.passwordRegex(newPassword.editText!!.text.toString()))
                            } catch (e: NoErrorException) {
                                viewModel.changePassword(
                                    oldPassword.editText!!.text.toString(),
                                    newPassword.editText!!.text.toString()
                                )
                            }
                        }
                        /**
                         * error when wrong old password
                         * */
                        is Resource.Error -> {
                            binding.oldPassword.error = getString(R.string.wrong_password)
                        }
                    }
                }
            }
            changePasswordLiveData.observe { resource ->
                binding.apply {
                    when (resource) {
                        is Resource.Loading -> {

                        }
                        /**
                         * after change password success will back to main activity and show toast
                         * */
                        is Resource.Success -> {
                            finish()
                            showToast(getString(R.string.change_password_success))
                        }
                        /**
                         * here is unknown error
                         * */
                        is Resource.Error -> {
                            changePasswordError.visibility = View.VISIBLE
                        }
                    }
                }
            }
            internetError.observe {
                binding.changePasswordError.apply {
                    visibility = View.VISIBLE
                    setText(R.string.no_internet)
                }
            }
        }
    }

    override fun createUI() {
        setUpActionBar()
        actionBar.setDisplayHomeAsUpEnabled(true)
        setUpEvent()
        setUpObserver()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override val viewModel: ChangePasswordViewModel by viewModels()
}