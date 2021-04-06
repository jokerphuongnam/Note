package com.example.note.ui.login

import androidx.lifecycle.ViewModel
import com.example.note.model.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCase: LoginUseCase) : ViewModel() {
}