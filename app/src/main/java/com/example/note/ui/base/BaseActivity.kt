package com.example.note.ui.base

import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import com.example.note.R

abstract class BaseActivity<BD : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes override val layoutRes: Int
) : AppCompatActivity(), BaseUI<BD, VM> {

    override val binding: BD by lazy {
        DataBindingUtil.setContentView<BD>(this, layoutRes).apply {
            lifecycleOwner = this@BaseActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        action()
    }

    inline fun <reified F : Fragment> navigateFragment(
        @LayoutRes resFragment: Int,
        vararg params: Any,
        tag: String,
        @AnimRes start: Int? = null,
        @AnimRes end: Int? = null
    ) {
        val supportFragmentManager: FragmentManager = supportFragmentManager
        val fragment: Fragment =
            supportFragmentManager.findFragmentByTag(tag) ?: F::class.constructors.find {
                it.parameters.size == params.size
            }?.call(params)!!
        supportFragmentManager.commit {
            addToBackStack(null)
            if (start != null && end != null) {
                setCustomAnimations(start, end)
            }
            replace(resFragment, fragment, tag)
        }
    }

    protected val isEmptyFragmentBackStack: Boolean
        get() = supportFragmentManager.backStackEntryCount == 0

    private var clickFirstTime: Long = 0

    protected fun twiceTimeToExit() {
        if (clickFirstTime == 0L) {
            clickFirstTime = System.currentTimeMillis()
            showToast(getString(R.string.mess_when_click_back_btn))
        } else {
            if (System.currentTimeMillis() - clickFirstTime < 2000L) {
                finishAffinity()
            }
        }
    }

//    override val actionBarSize: Int by lazy {
//        TypedValue.complexToDimensionPixelSize(TypedValue().data, resources.displayMetrics)
//    }
}