package com.example.note.ui.base

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.google.android.material.appbar.AppBarLayout

interface BaseUI<BD : ViewDataBinding, VM : BaseViewModel> {
    val layoutRes: Int
    fun createUI()
    val binding: BD
    val viewModel: VM

    fun <T> LiveData<T>.observe(observer: Observer<T>) {
        type({
            observe(viewLifecycleOwner, observer)
        }) {
            observe(this, observer)
        }
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        type({
            Toast.makeText(requireContext(), message, duration).show()
        }) {
            Toast.makeText(this, message, duration).show()
        }
    }

    private fun type(
        fragment: (Fragment.() -> Unit)? = null,
        activity: (AppCompatActivity.() -> Unit)? = null
    ) {
        when (this) {
            is Fragment -> {
                fragment ?: return
                fragment()
            }
            is AppCompatActivity -> {
                activity ?: return
                activity()
            }
        }
    }

//    val actionBarSize: Int

    fun <VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.setSource() {

    }

    /**
     * observer internet error if Activity or Fragment need this case will call this func
     * */
    fun noInternetError() {
        viewModel.internetError.observe {
            type({
                showToast(getString(R.string.no_internet))
            }) {
                showToast(getString(R.string.no_internet))
            }
        }
    }

    fun View.setPaddingAnimation(left: Int, top: Int, right: Int, bottom: Int) {
        ValueAnimator.ofInt(0, left, top, right, bottom).apply {
            duration = 1000
            addListener {
                setPadding(left, top, right, bottom)
            }
            start()
        }
    }

    fun View.setViewHeightAnimation(height: Int) {
        ValueAnimator.ofInt(0, height).apply {
            val params = layoutParams
            params.height = height
            layoutParams = params
        }
    }

    fun View.setScrollFlag(flags: Int){
        val params: AppBarLayout.LayoutParams = layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = flags
        layoutParams = params
    }

    fun Intent.putParcelableExtra(key: String, value: Parcelable) {
        putExtra(key, value)
    }
}