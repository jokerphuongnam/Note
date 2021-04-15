package com.example.note.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.databinding.ItemTaskBinding
import com.example.note.model.database.domain.Task


class TasksAdapter(private val deleteCallBack: (Task) -> Unit) :
    ListAdapter<Task, TasksAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder.create(parent, viewType, deleteCallBack)

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Task> by lazy {
            object : DiffUtil.ItemCallback<Task>() {
                override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                    oldItem.taskId == newItem.taskId

                override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                    oldItem.detail.trim().equals(newItem.detail.trim(), ignoreCase = true)
            }
        }
    }

    class TaskViewHolder private constructor(
        private val binding: ItemTaskBinding,
        private val deleteCallBack: (Task) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task = task
            binding.deleteTaskBtn.setOnClickListener {
                deleteCallBack.invoke(task)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                viewType: Int,
                deleteCallBack: (Task) -> Unit
            ): TaskViewHolder = TaskViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_task,
                    parent,
                    false
                ),
                deleteCallBack
            )
        }
    }
}