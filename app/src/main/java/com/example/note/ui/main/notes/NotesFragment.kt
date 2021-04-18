package com.example.note.ui.main.notes

import android.content.Context
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.R
import com.example.note.databinding.FragmentNotesBinding
import com.example.note.model.database.domain.Note
import com.example.note.ui.adapter.NotesAdapter
import com.example.note.ui.base.BaseFragment
import com.example.note.ui.main.MainActivity
import com.example.note.ui.noteinfo.NoteInfoActivity
import com.example.note.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NotesFragment : BaseFragment<FragmentNotesBinding, NotesViewModel>(R.layout.fragment_notes) {

    private val notesAdapter: NotesAdapter by lazy {
        NotesAdapter(selectedItem, deleteItem)
    }

    private val selectedItem: (Note) -> Unit by lazy {
        { note ->
            val context: Context = requireActivity()
            val intent = Intent(context, NoteInfoActivity::class.java)
            intent.putExtra(NoteInfoActivity.NOTE, note.nid)
            when (context) {
                is MainActivity -> {
                    context.addContent.launch(intent)
                }
            }
        }
    }

    private val deleteItem: (Note) -> Unit by lazy {
        { note ->
            viewModel.delete(note)
        }
    }

    private fun initNotesRecycler() {
        binding.notesRecycler.apply {
            adapter = notesAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }

    override fun createUI() {
        initNotesRecycler()
        binding.notesRefresh.apply {
            setOnRefreshListener { refreshNotes() }
            isRefreshing = false
        }
        viewModel.apply {
            noteLiveData.observe { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        notesAdapter.submitData(lifecycle, resource.data!!)
                        binding.notesRefresh.isRefreshing = false
                    }
                    is Resource.Error -> {

                    }
                }
            }
            deleteLiveData.observe{resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        showToast(getString(R.string.delete_success))
                    }
                    is Resource.Error -> {
                        showToast(getString(R.string.delete_error))
                    }
                }
            }
        }
    }

    private fun refreshNotes() {
        notesAdapter.refresh()
    }

    override val viewModel: NotesViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is MainActivity -> {
                context.refreshSubscription().subscribe {
                    notesAdapter.refresh()
                }
            }
        }
    }
}