package com.example.note.ui.noteinfo

import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.R
import com.example.note.databinding.ActivityNoteInfoBinding
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.ui.adapter.TasksAdapter
import com.example.note.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteInfoActivity :
    BaseActivity<ActivityNoteInfoBinding, NoteInfoViewModel>(R.layout.activity_note_info) {

    private val newNote: Note by lazy { Note() }
    private val tasksInfoAdapter: TasksAdapter by lazy { TasksAdapter() }
    private val actionBar: ActionBar by lazy { supportActionBar!! }

    private fun recyclerViewSetUp(){
        binding.apply {
            tasks.apply {
                adapter = tasksInfoAdapter.apply {
                    submitList(newNote.tasks)
                }
                layoutManager = LinearLayoutManager(this@NoteInfoActivity)
            }
        }
    }

    override fun action() {
        setSupportActionBar(binding.addToolBar)
        actionBar.setDisplayHomeAsUpEnabled(true)
        binding.apply {
            note = newNote
            addTask.setOnClickListener {
                /**
                 * before set tasks need change address of task if don't this will can't submit task
                 * because submitList has (oldList == newList) {don't do}
                 * */
                tasksInfoAdapter.submitList(null)
                tasksInfoAdapter.submitList(newNote.tasks.apply {
                    add(Task(false, "", newNote.nid))
                })
            }
        }
        viewModel.delete.observe {
            tasksInfoAdapter.submitList(newNote.tasks)
        }
        recyclerViewSetUp()
    }

    override val viewModel: NoteInfoViewModel by viewModels()

    /**
     * this event will enable the back
     * function to the button on press
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}