package com.moidot.moidot.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class ResponseVoteStatus(
    val `data`: Data,
) : BaseResponse() {
    @Parcelize
    data class Data(
        val endAt: String,
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
    ) : Parcelable {
        @Parcelize
        data class VoteStatuses(
            val bestPlaceId: Int,
            val isVoted: Boolean,
            val latitude: Double,
            val longitude: Double,
            val placeName: String,
            val votes: Int
        ) : Parcelable
    }
}