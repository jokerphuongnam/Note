package com.example.note.ui.main.userinfo

import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.note.R
import com.example.note.databinding.FragmentUserInfoBinding
import com.example.note.model.database.domain.User
import com.example.note.ui.base.BaseFragment
import com.example.note.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment :
    BaseFragment<FragmentUserInfoBinding, UserInfoViewModel>(R.layout.fragment_user_info) {

    private val userObservable: Observer<Resource<User>> by lazy {
        Observer<Resource<User>> { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.avatar.setImageResource(R.drawable.ic_loading)
                }
                is Resource.Success -> {
                    binding.user = resource.data
                    initAction()
                }
                is Resource.Error -> {
                    binding.avatar.setImageResource(R.drawable.ic_empty)
                }
            }
        }
    }

    private fun initAction() {
        binding.apply {
            edit.setOnClickListener {
                userControl.showNext()
                changeTheAbilityToEnterEditText(
                    firstName,
                    lastName,
                    ability = true
                )
                viewModel.editUser = viewModel.currentUser.copy()
                user = viewModel.editUser
            }
            okBtn.setOnClickListener {
                userControl.showPrevious()
                viewModel.editProfile()
            }
            cancelBtn.setOnClickListener {
                userControl.showPrevious()
                user = viewModel.currentUser
            }
        }
    }

    override fun createUI() {
        viewModel.userLiveData.observe(userObservable)
    }

    override val viewModel: UserInfoViewModel by viewModels()

    private fun changeTheAbilityToEnterEditText(vararg editText: EditText, ability: Boolean) {
        editText.forEach {
            it.isEnabled = ability
        }
    }
}