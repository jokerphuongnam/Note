package com.example.note.ui.main.notes

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.R
import com.example.note.databinding.FragmentNotesBinding
import com.example.note.ui.adapter.NotesAdapter
import com.example.note.ui.base.BaseFragment
import com.example.note.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : BaseFragment<FragmentNotesBinding, NotesViewModel>(R.layout.fragment_notes) {

    private val notesAdapter: NotesAdapter by lazy {
        NotesAdapter()
    }

    override fun action() {
        binding.notesRecycler.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        }
        viewModel.noteLiveData.observe { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    notesAdapter.submitData(lifecycle, resource.data!!)
                }
                is Resource.Error -> {

                }
            }
        }
    }

    override val viewModel: NotesViewModel by viewModels()
}