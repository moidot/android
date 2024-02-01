package com.moidot.moidot.presentation.onboard.viewmodel

import androidx.lifecycle.ViewModel
import com.moidot.moidot.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun saveOnboardDone() {
        userRepository.saveOnboardDoneState(true)
    }
}