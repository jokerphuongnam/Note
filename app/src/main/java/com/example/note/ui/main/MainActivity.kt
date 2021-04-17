package com.example.note.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
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
import com.example.note.ui.noteinfo.NoteInfoActivity
import com.example.note.utils.Resource
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.rbddevs.splashy.Splashy
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.subjects.PublishSubject

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


    fun refreshSubscription(): PublishSubject<Int> = refreshPublisher

    /**
     * when onNext Publisher will send broadcast for activity subscribe
     * */
    private val refreshPublisher: PublishSubject<Int> by lazy {
        PublishSubject.create()
    }

    private val loginContent: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> }

    private val loginIntent by lazy {
        Intent(this, LoginActivity::class.java)
    }

    private val addIntent: Intent by lazy {
        Intent(this, NoteInfoActivity::class.java)
    }

    val addContent: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                refreshPublisher.onNext(0)
            }
        }

    private val tabSelectedCallBack: TabLayout.OnTabSelectedListener by lazy {
        object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.mainViewPager.setCurrentItem(tab!!.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        }
    }

    /**
     * when user selected first tab ui will change:
     * - show fab add
     * - show bottom appbar
     * - allow exit appbar
     * */
    private fun changeUIWhenSelectedFirstTab() {
        binding.apply {
            tabWrap.animate().translationY(0F)
            actionBar.apply {
                setScrollFlag(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED)
                animate().translationY(0F)
                visibility = View.VISIBLE
            }
            addBtn.show()
        }
    }

    /**
     * when user selected other first tab ui will change:
     * - hide fab add
     * - hide bottom appbar
     * - don't allow exit appbar
     * */
    private fun changeUIWhenSelectedOtherTab() {
        binding.apply {
            tabWrap.animate()
                .translationY(tabWrap.height.toFloat())
            actionBar.apply {
                setScrollFlag(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL)
                animate().translationY(-actionBar.height.toFloat())
                visibility = View.GONE
            }
            addBtn.hide()
        }
    }

    private val addAction: View.OnClickListener by lazy {
        View.OnClickListener {
            addContent.launch(addIntent)
        }
    }

    private fun splashyScreen() {
        @Suppress("DEPRECATION")
        Handler().postDelayed({
            if (isRun) {
                binding.viewSwitch.showNext()
                initAction(viewModel.uidLiveData.value!!)
            } else {
                isRun = true
            }
        }, 2000)
        Splashy(this)
            .setLogo(R.drawable.ic_logo)
            .setTitle(R.string.app_name)
            .setAnimation(Splashy.Animation.SLIDE_IN_TOP_BOTTOM)
            .setDuration(2000)
            .show()

    }

    private var isRun = false

    /**
     * if have data will set event to link pager with tabs
     * */
    private fun setUpUI() {
        binding.apply {
            mainViewPager.apply {
                currentItem = 0
                adapter = mainAdapter
                registerOnPageChangeCallback(
                    object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            tabs.selectTab(tabs.getTabAt(position))
                            if (position != 0) {
                                changeUIWhenSelectedOtherTab()
                            } else {
                                changeUIWhenSelectedFirstTab()
                            }
                        }
                    }
                )
            }
            tabs.addOnTabSelectedListener(tabSelectedCallBack)
            addBtn.setOnClickListener(addAction)
        }
    }

    private fun initAction(resource: Resource<Long>) {
        when (resource) {
            is Resource.Loading -> {

            }
            is Resource.Success -> {
                if (isRun) {
                    setUpUI()
                } else {
                    isRun = true
                }
            }
            is Resource.Error -> {
                if (isRun) {
                    logout()
                } else {
                    isRun = true
                }
            }
        }
    }

    override fun action() {
        if (viewModel.isInit) {
            viewModel.isInit = true
        } else {
            splashyScreen()
        }
        viewModel.uidLiveData.observe { resource ->
            initAction(resource)
        }
    }

    private fun logout() {
        loginContent.launch(loginIntent)
    }


    override val viewModel: MainViewModel by viewModels()
}