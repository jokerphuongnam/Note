package com.example.note.ui.main.notes

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.example.note.R
import com.example.note.databinding.FragmentNoteInfoBinding
import com.example.note.databinding.FragmentNotesBinding
import com.example.note.model.database.domain.Note
import com.example.note.ui.adapter.NotesAdapter
import com.example.note.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment: BaseFragment<FragmentNotesBinding, NotesViewModel>(R.layout.fragment_notes) {

    private val notesAdapter: NotesAdapter by lazy {
        NotesAdapter()
    }

    override fun action() {
        binding.notesRecycler.adapter = notesAdapter
        viewModel.noteLiveData.observe {notes->
            notesAdapter.submitData(lifecycle, notes)
        }
    }

    override val viewModel: NotesViewModel by viewModels()
}