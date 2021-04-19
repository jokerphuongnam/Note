package com.example.note.ui.forgotpassword

import com.example.note.model.usecase.ForgotPasswordUseCase
import com.example.note.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(useCase: ForgotPasswordUseCase): BaseViewModel() {
}