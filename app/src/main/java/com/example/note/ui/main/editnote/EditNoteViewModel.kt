package com.example.note.ui.main.editnote

import androidx.lifecycle.ViewModel
import com.example.note.model.usecase.EditNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(private val useCase: EditNoteUseCase) : ViewModel() {
}