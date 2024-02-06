package com.moidot.moidot.data.remote.datasource.group

import com.moidot.moidot.data.remote.response.ResponseBestRegion

interface GroupPlaceRemoteDataSource {

    suspend fun getBestRegions(groupId:Int): Result<ResponseBestRegion>

}