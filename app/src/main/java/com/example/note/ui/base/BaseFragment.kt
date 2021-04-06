package com.example.note.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<BD : ViewDataBinding, VM : ViewModel>(
    @LayoutRes override val layoutRes: Int
) : Fragment(), BaseUI<BD, VM> {

    override val binding: BD by lazy {
        DataBindingUtil.inflate<BD>(layoutInflater, layoutRes, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
    }

    private var container: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.container = container
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        action()
    }
}