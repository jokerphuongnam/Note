package com.example.note.ui.register

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.example.note.R
import com.example.note.databinding.ActivityRegisterBinding
import com.example.note.ui.base.BaseActivity
import com.example.note.utils.Resource
import com.example.note.utils.nameRegex
import com.example.note.utils.passwordRegex
import com.example.note.utils.usernameRegex
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RegisterActivity :
    BaseActivity<ActivityRegisterBinding, RegisterViewModel>(R.layout.activity_register) {

    private val imageChoose: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri->
                    MediaStore.Images.Media.getBitmap(contentResolver, uri).let { bitmap ->
                        viewModel.avatar = bitmap
                        binding.image.setImageBitmap(bitmap)
                        binding.imageLayout.displayedChild = 1
                    }
                }
            }
        }

    private val takePhotoFromCamera: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                (result.data?.extras?.get("data") as Bitmap).apply {
                    viewModel.avatar = this
                    binding.image.setImageBitmap(this)
                    binding.imageLayout.displayedChild = 1
                }
            }
        }

    private lateinit var actionBar: ActionBar

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * when click ok button of date picker dialog will set time for birthday of user
     * */
    private val datePickerCallBack: DatePickerDialog.OnDateSetListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.currentUser.value!!.birthDay = calendar.timeInMillis
            viewModel.currentUser.postValue(viewModel.currentUser.value!!)
        }
    }

    private val datePicker: DatePickerDialog
        get() {
            val calendar: Calendar = viewModel.currentUser.value!!.birthDayCalendar
            return DatePickerDialog(
                this,
                datePickerCallBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            )
        }

    /**
     * check regex for user
     * if satisfy will to catch
     * */
    private val registerAction: View.OnClickListener by lazy {
        View.OnClickListener {
            binding.apply {
                try {
                    if (!password.editText?.text.toString()
                            .equals(repeatPassword.editText?.text.toString())
                    ) {
                        repeatPassword.error =
                            getString(R.string.repeat_password_other_new_password)
                    } else {
                        email.error = getString(user!!.username.usernameRegex())
                    }
                } catch (usernameError: Exception) {
                    try {
                        password.error =
                            getString(password.editText!!.text.toString().passwordRegex())
                    } catch (passwordError: Exception) {
                        try {
                            firstName.error = getString(user!!.fname.nameRegex())
                        } catch (firstNameError: Exception) {
                            try {
                                lastName.error = getString(user!!.lname.nameRegex())
                            } catch (lastNameError: Exception) {
                                viewModel.register(password.editText!!.text.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    private val chooseCalendar: View.OnClickListener by lazy {
        View.OnClickListener {
            datePicker.show()
        }
    }

    override fun createUI() {
        setUpToolbar()
        binding.apply {
            register.setOnClickListener(registerAction)
            calendarChoose.setOnClickListener(chooseCalendar)
            viewModel.currentUser.observe {
                user = it
            }
        }
        viewModel.registerLiveData.observe { resource ->
            binding.apply {
                when (resource) {
                    is Resource.Loading -> {
                        resetError(
                            email,
                            password,
                            repeatPassword,
                            firstName,
                            lastName
                        )
                    }
                    is Resource.Success -> {
                        finish()
                    }
                    is Resource.Error -> {
                        email.error = getString(R.string.the_email_same)
                    }
                }
            }
        }
        binding.apply {
            addImage.setOnClickListener {
                imageChoose.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "image/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                })
            }
            openCamera.setOnClickListener {
                takePhotoFromCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            }
            deleteImage.setOnClickListener {
                imageLayout.displayedChild = 0
                image.setImageBitmap(null)
                viewModel.avatar = null
            }
        }
    }

    private fun resetError(vararg textInputLayouts: TextInputLayout) {
        textInputLayouts.forEach { textInputLayout ->
            textInputLayout.error = null
        }
    }

    /**
     * this event will enable the back
     * function to the button on press
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override val viewModel: RegisterViewModel by viewModels()
}