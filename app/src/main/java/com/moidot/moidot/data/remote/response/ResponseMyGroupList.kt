package com.moidot.moidot.data.remote.response

data class ResponseMyGroupList(
    val data: List<Data>,
) : BaseResponse() {
    data class Data(
        //val bestPlaceNames: List<String>,
        //val confirmPlace: String,
        //val groupAdminName: String,
        val groupDate: String,
        val groupId: Int,
        val groupName: String,
        //val groupParticipates: Int,
        //val participantNames: List<String>
    )
}