package com.moidot.moidot.data.remote.response

data class ResponseUsersVotePlaceInfo(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val totalVoteNum: Int,
        val voteParticipations: List<VoteParticipation>
    ) {
        data class VoteParticipation(
            val isAdmin: Boolean,
            val nickName: String,
            val participationId: Int,
            val userId: Int,
        )
    }

}