package com.moidot.moidot.data.remote.response

data class ResponseGroupInfo(
    val `data`: Data,
) : BaseResponse() {
    data class Data(
        val adminEmail: String,
        val date: String,
        val groupId: Int,
        val name: String,
        val participantsByRegion: List<ParticipantsByRegion>
    ) {
        data class ParticipantsByRegion(
            val participations: List<Participation>,
            val regionName: String
        ) {
            data class Participation(
                val locationName: String,
                val participationId: Int,
                val transportation: String,
                val userEmail: String,
                val userName: String
            )
        }
    }
}