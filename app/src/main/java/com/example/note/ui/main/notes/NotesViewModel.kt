package com.example.note.ui.main.notes

import androidx.lifecycle.ViewModel
import com.example.note.model.usecase.NotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val useCase: NotesUseCase): ViewModel() {

}
