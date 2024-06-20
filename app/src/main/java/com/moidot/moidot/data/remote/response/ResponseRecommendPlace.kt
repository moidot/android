package com.moidot.moidot.data.remote.response

data class ResponseRecommendPlace(
    val code: Int,
    val `data`: List<Data>,
    val message: String
) {
    data class Data(
        val detail: Detail,
        val distance: String,
        val openTime: String,
        val tel: String,
        val thumUrl: String,
        val title: String
    ) {
        data class Detail(
            val address: String,
            val category: List<String>,
            val homePageUrl: String,
            val local: String,
            val menuInfo: List<String>,
            val openTime: String,
            val status: String,
            val tel: String,
            val thumUrls: List<String>,
            val title: String,
            val x: String,
            val y: String
        )
    }
}