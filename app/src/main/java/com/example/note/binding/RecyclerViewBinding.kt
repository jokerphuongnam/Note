package com.example.note.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewBinding {
    @BindingAdapter("adapter")
    fun <VH: RecyclerView.ViewHolder>setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<VH>){
        view.adapter = adapter
    }
}