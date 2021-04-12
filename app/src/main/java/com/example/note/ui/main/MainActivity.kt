package com.example.note.ui.main

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.note.R
import com.example.note.databinding.ActivityMainBinding
import com.example.note.ui.adapter.MainFragmentAdapter
import com.example.note.ui.base.BaseActivity
import com.example.note.ui.login.LoginActivity
import com.example.note.ui.main.notes.NotesFragment
import com.example.note.ui.main.setting.SettingFragment
import com.example.note.utils.Resource
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    private val mainAdapter: MainFragmentAdapter by lazy {
        MainFragmentAdapter(this@MainActivity).apply {
            addFragment(NotesFragment())
            addFragment(SettingFragment()) {
                logoutSubscription().subscribe {
                    logout()
                }
            }
        }
    }

    private val loginContent: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> }

    private val loginIntent by lazy {
        Intent(this, LoginActivity::class.java)
    }

    override fun action() {
        viewModel.uidLiveData.observe { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    /**
                     * if have data will set event to link pager with tabs
                     * */
                    binding.apply {
                        mainViewPager.apply {
                            currentItem = 0
                            adapter = mainAdapter
                            registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    binding.tabs.selectTab(binding.tabs.getTabAt(position))
                                    if (position != 0) {
                                        binding
                                    }
                                }
                            })
                        }

                        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                            override fun onTabSelected(tab: TabLayout.Tab?) {
                                binding.mainViewPager.setCurrentItem(tab!!.position, true)
                            }

                            override fun onTabUnselected(tab: TabLayout.Tab?) {
                            }

                            override fun onTabReselected(tab: TabLayout.Tab?) {
                            }
                        })
                    }
                }
                is Resource.Error -> {
                    logout()
                }
            }
        }
    }

    private fun logout() {
        loginContent.launch(loginIntent)
    }

    override val viewModel: MainViewModel by viewModels()
}