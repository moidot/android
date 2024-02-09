package com.moidot.moidot.presentation.main.group.space.member.vote.before

import android.os.Bundle
import android.view.View
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentMemberVoteBeforeBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.util.MarkerManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberVoteBeforeFragment : BaseFragment<FragmentMemberVoteBeforeBinding>(R.layout.fragment_member_vote_before) {

    private lateinit var kakaoMap: KakaoMap
    private lateinit var mapManager: MarkerManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()
    }

    private fun initMapView() {
        mapManager = MarkerManager(requireContext())
        binding.fgMemberVoteBeforeMapView.start(object : KakaoMapReadyCallback() {
            override fun getPosition(): LatLng {
                return LatLng.from(37.4005, 127.1101)
            }

            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
            }
        })
    }

}