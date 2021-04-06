package com.example.note.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.databinding.ItemNoteBinding
import com.example.note.model.database.domain.Note

class NotesAdapter : PagingDataAdapter<Note, NotesAdapter.NoteViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        getItem(position)?.let { note ->
            holder.bind(note)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class NoteViewHolder private constructor(biding: ItemNoteBinding) :
        RecyclerView.ViewHolder(biding.root) {

        fun bind(note: Note) {

        }

        companion object {
            fun create(parent: ViewGroup, viewType: Int): NoteViewHolder {
                return NoteViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_note,
                        parent,
                        false
                    )
                )
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> by lazy {
            object : DiffUtil.ItemCallback<Note>() {
                override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem.nid == newItem.nid
                }

                override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}