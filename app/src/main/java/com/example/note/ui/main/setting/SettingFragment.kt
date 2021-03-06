package com.example.note.ui.main.setting

import android.content.Intent
import androidx.fragment.app.viewModels
import com.example.note.R
import com.example.note.databinding.FragmentSettingBinding
import com.example.note.ui.base.BaseFragment
import com.example.note.ui.changepassword.ChangePasswordActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.subjects.PublishSubject

@AndroidEntryPoint
class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingViewModel>(R.layout.fragment_setting) {
    override fun createUI() {
        viewModel.logoutLiveData.observe {

        }
        binding.apply {
            changePassword.setOnClickListener {
                startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
            }
            logout.setOnClickListener {
                viewModel.logout()
                logoutPublisher.onNext(0)
            }
        }
    }

    fun logoutSubscription(): PublishSubject<Int> = logoutPublisher

    /**
     * when onNext Publisher will send broadcast for activity subscribe
     * */
    private val logoutPublisher: PublishSubject<Int> by lazy {
        PublishSubject.create()
    }

    override val viewModel: SettingViewModel by viewModels()
}