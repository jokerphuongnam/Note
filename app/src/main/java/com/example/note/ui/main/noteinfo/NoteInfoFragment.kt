package com.example.note.ui.main.noteinfo

import androidx.fragment.app.viewModels
import com.example.note.R
import com.example.note.databinding.FragmentNoteInfoBinding
import com.example.note.ui.base.BaseFragment

class NoteInfoFragment: BaseFragment<FragmentNoteInfoBinding, NoteInfoViewModel>(R.layout.fragment_note_info){
    override fun action() {

    }

    override val viewModel: NoteInfoViewModel by viewModels()
}