package com.example.note.ui.noteinfo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.note.R
import com.example.note.databinding.ActivityNoteInfoBinding
import com.example.note.model.database.domain.Task
import com.example.note.ui.adapter.ImageAdapter
import com.example.note.ui.adapter.TasksAdapter
import com.example.note.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteInfoActivity :
    BaseActivity<ActivityNoteInfoBinding, NoteInfoViewModel>(R.layout.activity_note_info) {

    private val imageChoose: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    MediaStore.Images.Media.getBitmap(contentResolver, uri).let { bitmap ->
                        imageAdapter.addBitmap(bitmap)
                        viewModel.images.add(bitmap)
                    }
                }
            }
        }

    private val takePhotoFromCamera: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                (result.data?.extras?.get("data") as Bitmap).apply {
                    imageAdapter.addBitmap(this)
                    viewModel.images.add(this)
                }
            }
        }

    private val tasksInfoAdapter: TasksAdapter by lazy {
        TasksAdapter {
            viewModel.newNote.value!!.tasks.remove(it)
            tasksInfoAdapter.submitList(viewModel.newNote.value!!.tasks)
        }
    }

    private val imageAdapter: ImageAdapter by lazy {
        ImageAdapter { size ->
            if (size == 0) {
                binding.images.visibility = View.GONE
            } else {
                binding.images.visibility = View.VISIBLE
            }
        }
    }

    private val actionBar: ActionBar by lazy { supportActionBar!! }

    private fun recyclerViewSetUp() {
        binding.apply {
            tasks.apply {
                adapter = tasksInfoAdapter
                layoutManager =
                    LinearLayoutManager(this@NoteInfoActivity, RecyclerView.VERTICAL, false)
            }
            images.apply {
                adapter = imageAdapter
                layoutManager =
                    StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            }
        }
    }

    override fun createUI() {
        val insertType: InsertType? = intent.getParcelableExtra(INSERT_TYPE)
        viewModel.initNote(intent.getLongExtra(NOTE, INSERT), insertType)
        noInternetError()
        setSupportActionBar(binding.addToolBar)
        binding.tabWrap.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.image_choose -> {
                    imageChoose.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        type = "image/*"
                        addCategory(Intent.CATEGORY_OPENABLE)
                    })
                    true
                }
                R.id.take_photo -> {
                    takePhotoFromCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    true
                }
                else -> {
                    false
                }
            }
        }
        actionBar.setDisplayHomeAsUpEnabled(true)
        viewModel.newNote.observe {
            binding.note = it
            tasksInfoAdapter.submitList(it.tasks)
            imageAdapter.addUrl(it.images)
        }
        binding.apply {
            addTask.setOnClickListener {
                /**
                 * before set tasks need change address of task if don't this will can't submit task
                 * because submitList has (oldList == newList) {don't do}
                 * */
                tasksInfoAdapter.submitList(viewModel.newNote.value!!.tasks.apply {
                    add(Task())
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
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        const val INSERT_TYPE: String = "insertType"
    }
}