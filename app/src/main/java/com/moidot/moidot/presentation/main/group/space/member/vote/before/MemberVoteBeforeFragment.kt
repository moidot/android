package com.moidot.moidot.presentation.main.group.space.member.vote.before

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.OrderingType
import com.moidot.moidot.R
import com.moidot.moidot.data.remote.response.ResponseBestRegion
import com.moidot.moidot.databinding.FragmentMemberVoteBeforeBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.util.Constant.MEMBER_VOTE_EXTRA
import com.moidot.moidot.util.MarkerManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberVoteBeforeFragment : BaseFragment<FragmentMemberVoteBeforeBinding>(R.layout.fragment_member_vote_before) {

    private val bestRegions by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArray(MEMBER_VOTE_EXTRA) as Array<ResponseBestRegion.Data>
        } else {
            arguments?.getParcelableArray(MEMBER_VOTE_EXTRA) as Array<ResponseBestRegion.Data>
        }
    }

    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer
    private lateinit var mapManager: MarkerManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()
        Log.d("kite",bestRegions.toString())
    }

    private fun initMapView() {
        mapManager = MarkerManager(requireContext())
        binding.fgMemberVoteBeforeMapView.start(object : KakaoMapReadyCallback() {
            override fun getPosition(): LatLng {
                return LatLng.from(bestRegions[0].latitude, bestRegions[0].longitude)
            }

            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                addLeaderInfoMarker()
            }
        })
    }

    private fun addLeaderInfoMarker() {
        labelLayer = kakaoMap.labelManager!!.addLayer(
            LabelLayerOptions.from()
                .setOrderingType(OrderingType.Rank)
        )!!

        bestRegions.forEach {
            labelLayer.addLabel(
                LabelOptions.from(
                    "voteRegions", LatLng.from(it.latitude, it.longitude)
                ).setStyles(
                    LabelStyle.from(mapManager.getBestRegionPlaceMarker(it.name)).setApplyDpScale(false)
                )
            )
        }
    }
}