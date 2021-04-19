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
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserInfoFragment :
    BaseFragment<FragmentUserInfoBinding, UserInfoViewModel>(R.layout.fragment_user_info) {

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

    private val userObservable: Observer<Resource<User>> by lazy {
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

    private val datePickerCallBack: DatePickerDialog.OnDateSetListener by lazy {
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            viewModel.currentUser.value!!.birthDay = calendar.timeInMillis
            viewModel.currentUser.postValue(viewModel.currentUser.value!!)
        }
    }

    private val datePicker: DatePickerDialog
        get() {
            val date = Date(viewModel.currentUser.value!!.birthDay)
            return DatePickerDialog(
                requireContext(),
                datePickerCallBack,
                date.year + 1900,
                date.month,
                date.day
            )
        }

    private fun initAction() {
        binding.apply {
            viewModel.currentUser.observe{
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
        viewModel.userLiveData.observe(userObservable)
        viewModel.resultEditUserLiveData.observe(editProfileObserver)
    }

    private fun changeTheAbilityToEnterEditText(vararg view: View, ability: Boolean) {
        view.forEach {
            it.isEnabled = ability
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when(context){
            is MainActivity ->{
                context.cancelSubscription().subscribe {
                    binding.apply {
                        if(userControl.displayedChild == 1){
                            cancelBtn.performClick()
                        }
                    }
                }
            }
        }
    }

    override val viewModel: UserInfoViewModel by viewModels()
}