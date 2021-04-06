package com.example.note.ui.main

import androidx.activity.viewModels
import com.example.note.R
import com.example.note.databinding.ActivityMainBinding
import com.example.note.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override fun action() {

    }

    override val viewModel: MainViewModel by viewModels()
}