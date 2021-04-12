package com.example.note.ui.main.editnote

import com.example.note.model.usecase.EditNoteUseCase
import com.example.note.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(private val useCase: EditNoteUseCase) : BaseViewModel() {
}