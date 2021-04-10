package com.example.note.ui.main.setting

import androidx.fragment.app.viewModels
import com.example.note.R
import com.example.note.databinding.FragmentSettingBinding
import com.example.note.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment: BaseFragment<FragmentSettingBinding, SettingViewModel>(R.layout.fragment_setting) {
    override fun action() {

    }

    override val viewModel: SettingViewModel by viewModels()
}