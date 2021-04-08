package com.example.note.ui.base

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel

abstract class BaseActivity<BD : ViewDataBinding, VM : ViewModel>(
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
}