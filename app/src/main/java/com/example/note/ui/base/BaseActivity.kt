package com.example.note.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
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
}