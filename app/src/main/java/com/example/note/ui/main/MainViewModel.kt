package com.example.note.ui.main

import androidx.lifecycle.ViewModel
import com.example.note.model.usecase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: MainUseCase): ViewModel() {
}