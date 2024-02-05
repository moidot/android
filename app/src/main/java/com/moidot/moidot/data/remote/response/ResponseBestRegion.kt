package com.moidot.moidot.data.remote.response

data class ResponseBestRegion(
    val `data`: List<Data>,
) : BaseResponse() {
    data class Data(
        val latitude: Double,
        val longitude: Double,
        val moveUserInfo: List<MoveUserInfo>,
        val name: String
    ) {
        data class MoveUserInfo(
            val isAdmin: Boolean,
            val path: List<Path>,
            val payment: Int,
            val totalDistance: Int,
            val totalTime: Int,
            val transitCount: Int,
            val transportationType: String,
            val userId: Int,
            val userName: String
        ) {
            data class Path(
                val x: Double,
                val y: Double
            )
        }
    }
}