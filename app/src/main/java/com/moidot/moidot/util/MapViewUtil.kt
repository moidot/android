package com.moidot.moidot.util

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.moidot.moidot.data.remote.response.ResponseBestRegion

object MapViewUtil {

    // canShowMapPoints를 사용하여 특정 zoom level에서 해당 좌표들이 다 보이는 지 확인한 후 zoomLevel을 계산하여 반환한다.
    fun setZoomLevelByCheckRegionNameMapPoints(kakaoMap: KakaoMap, bestRegions: List<ResponseBestRegion.Data>): Int {
        val mapPoints = bestRegions.map { LatLng.from(it.latitude, it.longitude) }
        for (level in kakaoMap.maxZoomLevel downTo kakaoMap.minZoomLevel) {
            if (kakaoMap.canShowMapPoints(level, *mapPoints.toTypedArray())) {
                return level.minus(1)
            }
        }
        return kakaoMap.zoomLevel
    }

    fun setZoomLevelByCheckMapPoints(kakaoMap: KakaoMap, moveUserInfos: List<ResponseBestRegion.Data.MoveUserInfo>): Int {
        val mapPoints = moveUserInfos.map { LatLng.from(it.path[0].y, it.path[0].x) }
        for (level in kakaoMap.maxZoomLevel downTo kakaoMap.minZoomLevel) {
            if (kakaoMap.canShowMapPoints(level, *mapPoints.toTypedArray())) {
                return level.minus(1)
            }
        }
        return kakaoMap.zoomLevel
    }
}