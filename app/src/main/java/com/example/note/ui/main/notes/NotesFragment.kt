package com.example.note.ui.main.notes

import androidx.fragment.app.viewModels
import com.example.note.R
import com.example.note.databinding.FragmentNoteInfoBinding
import com.example.note.ui.base.BaseFragment

class NotesFragment: BaseFragment<FragmentNoteInfoBinding, NotesViewModel>(R.layout.fragment_notes) {
    override fun action() {

    }

    override val viewModel: NotesViewModel by viewModels()
}