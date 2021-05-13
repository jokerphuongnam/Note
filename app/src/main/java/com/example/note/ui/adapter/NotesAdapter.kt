package com.example.note.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.databinding.ItemNoteBinding
import com.example.note.model.database.domain.Note

class NotesAdapter(private val itemClick: (Note) -> Unit, private val longClick: (Note) -> Unit) :
    PagingDataAdapter<Note, NotesAdapter.NoteViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder.create(parent, viewType, itemClick, longClick)

    class NoteViewHolder private constructor(
        private val biding: ItemNoteBinding,
        private val itemClick: (Note) -> Unit,
        private val longClick: (Note) -> Unit
    ) :
        RecyclerView.ViewHolder(biding.root) {

        fun bind(noteOptional: Note?) {
            biding.note = noteOptional
            noteOptional?.let { note ->
                biding.noteItem.apply {
                    setOnClickListener {
                        itemClick(note)
                    }
                    setOnLongClickListener {
                        PopupMenu(context, it).apply {
                            setOnMenuItemClickListener {item ->
                                when (item.itemId) {
                                    R.id.edit -> {
                                        itemClick(note)
                                        true
                                    }
                                    R.id.delete -> {
                                        longClick(note)
                                        true
                                    }
                                    else -> false
                                }
                            }
                            inflate(R.menu.note_item_control)
                            show()
                        }
                        true
                    }
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup, viewType: Int,
                itemClick: (Note) -> Unit,
                longClick: (Note) -> Unit
            ): NoteViewHolder {
                return NoteViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_note,
                        parent,
                        false
                    ),
                    itemClick,
                    longClick
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