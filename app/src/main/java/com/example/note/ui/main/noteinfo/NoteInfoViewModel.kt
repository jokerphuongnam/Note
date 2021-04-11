package com.example.note.ui.main.noteinfo

import androidx.lifecycle.ViewModel
import com.example.note.model.usecase.NoteInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteInfoViewModel @Inject constructor(private val useCase: NoteInfoUseCase) : ViewModel() {

}
