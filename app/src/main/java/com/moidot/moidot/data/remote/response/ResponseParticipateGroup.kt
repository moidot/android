package com.moidot.moidot.data.remote.response

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

data class ResponseParticipateGroup(
    val data: List<Data>,
) : BaseResponse() {
    data class Data(
        val bestPlaceNames: List<String>,
        val confirmPlace: String,
        val groupAdminName: String,
        val groupDate: String,
        val groupId: Int,
        val groupName: String,
        val groupParticipates: Int,
        val participantNames: List<String>,
        val isAdmin: Boolean,
        val isStartVote: Boolean,
    ) {
        @SuppressLint("SimpleDateFormat")
        fun getFormattedDate(): String? {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("yyyy.MM.dd")
            val date = inputFormat.parse(groupDate)
            return date?.let { outputFormat.format(it) }
        }
    }
}
