package com.moidot.moidot.presentation.main.group.space.leader.vote.before

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.moidot.moidot.databinding.FragmentLeaderVoteBeforeBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.presentation.main.group.space.leader.vote.create.CreateVoteActivity
import com.moidot.moidot.presentation.main.group.space.member.vote.before.MemberVoteBeforeViewModel
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.VOTE_RECREATE_STATE
import com.moidot.moidot.util.MapViewUtil
import com.moidot.moidot.util.MarkerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LeaderVoteBeforeFragment : BaseFragment<FragmentLeaderVoteBeforeBinding>(R.layout.fragment_leader_vote_before) {

    private val groupId by lazy { arguments?.getInt(GROUP_ID) ?: -1 }
    private val voteRecreateState by lazy { arguments?.getBoolean(VOTE_RECREATE_STATE) ?: false }

    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer
    private lateinit var mapManager: MarkerManager

    private val viewModel: MemberVoteBeforeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
        viewModel.loadBestRegions(groupId)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.bestRegions.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                val centerLatLang = async {
                    MapViewUtil.getMapCenterPointByMapPoints(it.map { LatLng.from(it.latitude, it.longitude) })
                }.await()
                initMapView(it, centerLatLang)
            }
        }
    }

    private fun initMapView(bestRegions: List<ResponseBestRegion.Data>, centerLatLang: LatLng) {
        mapManager = MarkerManager(requireContext())
        binding.fgLeaderVoteBeforeMapView.start(object : KakaoMapReadyCallback() {
            override fun getPosition(): LatLng {
                return LatLng.from(centerLatLang.latitude, centerLatLang.longitude)
            }

            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                addLeaderInfoMarker(bestRegions)
                MapViewUtil.setZoomLevelByRegionNameMapPoints(kakaoMap, bestRegions)
            }
        })
    }

    private fun addLeaderInfoMarker(bestRegions: List<ResponseBestRegion.Data>) {
        labelLayer = kakaoMap.labelManager!!.addLayer(
            LabelLayerOptions.from()
                .setOrderingType(OrderingType.Rank)
        )!!

        bestRegions.forEach {
            labelLayer.addLabel(
                LabelOptions.from(
                    it.name, LatLng.from(it.latitude, it.longitude)
                ).setStyles(
                    LabelStyle.from(mapManager.getBestRegionPlaceMarker(it.name)).setApplyDpScale(false)
                )
            )
        }
    }

    // TODO 왜 데이터 바인딩 onclick 이 안 먹는 건지 이해 x
    fun onClickVoteCreate() {
        Intent(requireContext(), CreateVoteActivity::class.java).apply {
            putExtra(VOTE_RECREATE_STATE, voteRecreateState)
            startActivity(this)
        }
    }

}