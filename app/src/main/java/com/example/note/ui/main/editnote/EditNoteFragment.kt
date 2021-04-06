package com.example.note.ui.main.editnote

import androidx.fragment.app.viewModels
import com.example.note.R
import com.example.note.databinding.FragmentEditNoteBinding
import com.example.note.ui.base.BaseFragment

class EditNoteFragment: BaseFragment<FragmentEditNoteBinding, EditNoteViewModel>(R.layout.fragment_edit_note) {
    override val viewModel: EditNoteViewModel by viewModels()

    override fun action() {

    }
}