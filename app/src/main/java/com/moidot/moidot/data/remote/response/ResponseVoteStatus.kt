package com.moidot.moidot.data.remote.response

data class ResponseVoteStatus(
    val `data`: Data,
) : BaseResponse() {
    data class Data(
        val endAt: String?,
        val groupDate: String,
        val groupId: Int,
        val groupName: String,
        val isAnonymous: Boolean,
        val isClosed: Boolean,
        val isEnabledMultipleChoice: Boolean,
        val isVotingParticipant: Boolean,
        val totalVoteNum: Int,
        val voteId: Int,
        val voteStatuses: List<VoteStatuses>,
    ) {
        data class VoteStatuses(
            val bestPlaceId: Int,
            val isVoted: Boolean,
            val latitude: Double,
            val longitude: Double,
            val placeName: String,
            val votes: Int
        )
    }
}