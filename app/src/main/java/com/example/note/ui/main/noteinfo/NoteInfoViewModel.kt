package com.example.note.ui.main.noteinfo

import com.example.note.model.usecase.NoteInfoUseCase
import com.example.note.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteInfoViewModel @Inject constructor(private val useCase: NoteInfoUseCase) : BaseViewModel() {

}
