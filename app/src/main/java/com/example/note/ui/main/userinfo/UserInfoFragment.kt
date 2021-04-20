package com.example.note.ui.main.userinfo

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.note.R
import com.example.note.databinding.FragmentUserInfoBinding
import com.example.note.model.database.domain.User
import com.example.note.ui.base.BaseFragment
import com.example.note.ui.main.MainActivity
import com.example.note.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserInfoFragment :
    BaseFragment<FragmentUserInfoBinding, UserInfoViewModel>(R.layout.fragment_user_info) {

    /**
     * when finish edit user
     * if success will update for user
     * if error will update temp user
     * */
    private val editProfileObserver: Observer<Resource<User>> by lazy {
        Observer<Resource<User>> { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    viewModel.currentUser.postValue(resource.data)
                }
                is Resource.Error -> {
                    viewModel.currentUser.postValue(viewModel.tempUser)
                }
            }
        }
    }

    /**
     * after set user will set loading for avatar
     * if can't load image empty
     * */
    private val userObserver: Observer<Resource<User>> by lazy {
        Observer<Resource<User>> { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.avatar.setImageResource(R.drawable.ic_loading)
                }
                is Resource.Success -> {
                    binding.user = resource.data
                    initAction()
                }
                is Resource.Error -> {
                    binding.avatar.setImageResource(R.drawable.ic_empty)
                }
            }
        }
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
                requireContext(),
                datePickerCallBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            )
        }

    private fun initAction() {
        binding.apply {
            viewModel.currentUser.observe {
                user = it
            }
            edit.setOnClickListener {
                userControl.displayedChild = 1
                enableView()
                viewModel.tempUser = viewModel.currentUser.value!!.clone()
            }
            okBtn.setOnClickListener {
                userControl.displayedChild = 0
                viewModel.editProfile()
                disableView()
            }
            cancelBtn.setOnClickListener {
                userControl.displayedChild = 0
                viewModel.currentUser.postValue(viewModel.tempUser)
                disableView()
            }
            calendarChoose.setOnClickListener {
                datePicker.show()
            }
        }
    }

    private fun enableView() {
        binding.apply {
            calendarChoose.visibility = View.VISIBLE
            changeTheAbilityToEnterEditText(
                avatar,
                firstName,
                lastName,
                ability = true
            )
        }
    }

    private fun disableView() {
        binding.apply {
            calendarChoose.visibility = View.INVISIBLE
            changeTheAbilityToEnterEditText(
                avatar,
                firstName,
                lastName,
                ability = false
            )
        }
    }

    override fun createUI() {
        viewModel.userLiveData.observe(userObserver)
        viewModel.resultEditUserLiveData.observe(editProfileObserver)
    }

    private fun changeTheAbilityToEnterEditText(vararg view: View, ability: Boolean) {
        view.forEach {
            it.isEnabled = ability
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is MainActivity -> {
                context.cancelSubscription().subscribe {
                    binding.apply {
                        if (userControl.displayedChild == 1) {
                            cancelBtn.performClick()
                        }
                    }
                }
            }
        }
    }

    val isEditUse: Boolean get() = binding.userControl.displayedChild == 1

    override val viewModel: UserInfoViewModel by viewModels()
}