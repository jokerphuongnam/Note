package com.example.note.ui.noteinfo

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.R
import com.example.note.databinding.ActivityNoteInfoBinding
import com.example.note.model.database.domain.Task
import com.example.note.ui.adapter.TasksAdapter
import com.example.note.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteInfoActivity :
    BaseActivity<ActivityNoteInfoBinding, NoteInfoViewModel>(R.layout.activity_note_info) {
    private val tasksInfoAdapter: TasksAdapter by lazy {
        TasksAdapter {
            viewModel.newNote.value!!.tasks.remove(it)
            tasksInfoAdapter.submitList(viewModel.newNote.value!!.tasks)
        }
    }

    private val actionBar: ActionBar by lazy { supportActionBar!! }

    private fun recyclerViewSetUp() {
        binding.apply {
            tasks.apply {
                adapter = tasksInfoAdapter
                layoutManager = LinearLayoutManager(this@NoteInfoActivity)
            }
        }
    }

    override fun action() {
        viewModel.initNote(intent.getLongExtra(NOTE, INSERT))
        noInternetError()
        setSupportActionBar(binding.addToolBar)
        actionBar.setDisplayHomeAsUpEnabled(true)
        viewModel.newNote.observe{
            binding.note = it
            tasksInfoAdapter.submitList(it.tasks)
        }
        binding.apply {
            addTask.setOnClickListener {
                /**
                 * before set tasks need change address of task if don't this will can't submit task
                 * because submitList has (oldList == newList) {don't do}
                 * */
                tasksInfoAdapter.submitList(viewModel.newNote.value!!.tasks.apply {
                    add(Task(false, ""))
                }.toMutableList())
            }
        }
        recyclerViewSetUp()
    }

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

    override fun onBackPressed() {
        viewModel.saveNote()
        setResult(Activity.RESULT_OK, Intent())
        super.onBackPressed()
    }

    override val viewModel: NoteInfoViewModel by viewModels()

    companion object {
        const val NOTE: String = "note"
        const val INSERT: Long = -1
    }
}