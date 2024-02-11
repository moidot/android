package com.moidot.moidot.util

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt

object MapViewUtil {

    fun getMapCenterPointByMapPoints(coordinates: List<LatLng>): LatLng {
        return calculateCenter(coordinates)
    }

    // 좌표들을 통해 지도의 중심좌표를 계산하는 함수
    private fun calculateCenter(coordinates: List<LatLng>): LatLng {
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

    fun setZoomLevelByRegionNameMapPoints(kakaoMap: KakaoMap, bestRegions: List<ResponseBestRegion.Data>) {
        val mapPoints = bestRegions.map { LatLng.from(it.latitude, it.longitude) }    // 추천 장소 좌표들
        kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(getZoomLevelByCheckMapPoints(kakaoMap, mapPoints)))
    }

    fun setZoomLevelByUserLocationMapPoints(kakaoMap: KakaoMap, moveUserInfos: List<ResponseBestRegion.Data.MoveUserInfo>) {
        val mapPoints = moveUserInfos.map { LatLng.from(it.path[0].y, it.path[0].x) }     // 유저 정보 좌표들
        kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(getZoomLevelByCheckMapPoints(kakaoMap, mapPoints)))
    }

    // canShowMapPoints를 사용하여 특정 zoom level에서 해당 좌표들이 다 보이는 지 확인한 후 zoom level을 계산하여 반환한다.
    private fun getZoomLevelByCheckMapPoints(kakaoMap: KakaoMap, mapPoints: List<LatLng>): Int {
        for (level in kakaoMap.maxZoomLevel downTo kakaoMap.minZoomLevel) {
            if (kakaoMap.canShowMapPoints(level, *mapPoints.toTypedArray())) {
                return level.minus(1)
            }
        }
        return kakaoMap.zoomLevel
    }

}