package com.moidot.moidot.util

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt

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

    // 좌표들을 통해 지도의 중심좌표를 계산하는 함수
    fun calculateCenter(coordinates: List<LatLng>): LatLng {
        var x = 0.0
        var y = 0.0
        var z = 0.0

        coordinates.forEach {
            val latitude = Math.toRadians(it.latitude)
            val longitude = Math.toRadians(it.longitude)

            x += cos(latitude) * cos(longitude)
            y += cos(latitude) * sin(longitude)
            z += sin(latitude)
        }

        val total = coordinates.size

        x /= total
        y /= total
        z /= total

        val centralLongitude = atan2(y, x)
        val centralSquareRoot = sqrt(x * x + y * y)
        val centralLatitude = atan2(z, centralSquareRoot)

        return LatLng.from(Math.toDegrees(centralLatitude), Math.toDegrees(centralLongitude))
    }
}