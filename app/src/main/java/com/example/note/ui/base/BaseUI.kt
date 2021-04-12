package com.example.note.ui.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView

interface BaseUI<BD : ViewDataBinding, VM : BaseViewModel> {
    val layoutRes: Int
    fun action()
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

    fun <VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.setSource() {

    }

    /**
     * observer internet error if Activity or Fragment need this case will call this func
     * */
    fun noInternetError(){
        viewModel.internetError.observe{
            showToast("không có internet")
        }
    }
}