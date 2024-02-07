package com.moidot.moidot.presentation.main.group.space.leader.vote.create

import android.os.Bundle
import androidx.activity.viewModels
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityCreateVoteBinding
import com.moidot.moidot.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateVoteActivity : BaseActivity<ActivityCreateVoteBinding>(R.layout.activity_create_vote) {

    private val viewModel: CreateVoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    fun endTimeCheckListener() {
        viewModel.setHasEndTime(!viewModel.hasEndTime.value!!)
    }

    fun setDateInfoListener() {
        if (viewModel.hasEndTime.value == true) {

        }
    }

    fun setTimeInfoListener() {
        if (viewModel.hasEndTime.value == true) {

        }
    }


}