package com.moidot.moidot.presentation.main.group.space.common.place.view

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityRecommendPlaceBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.presentation.main.group.space.common.place.adapter.RecommendPlaceAdapter
import com.moidot.moidot.presentation.main.group.space.common.place.viewmodel.RecommendPlaceViewModel
import com.moidot.moidot.util.Constant.PLACE_NAME_EXTRA
import com.moidot.moidot.util.Constant.PLACE_X_EXTRA
import com.moidot.moidot.util.Constant.PLACE_Y_EXTRA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecommendPlaceActivity : BaseActivity<ActivityRecommendPlaceBinding>(R.layout.activity_recommend_place) {
    private val placeName by lazy { intent.getStringExtra(PLACE_NAME_EXTRA) ?: "" }
    private val placeX by lazy { intent.getDoubleExtra(PLACE_X_EXTRA, 0.0) }
    private val placeY by lazy { intent.getDoubleExtra(PLACE_Y_EXTRA, 0.0) }
    private val viewModel: RecommendPlaceViewModel by viewModels()
    private val recommendPlaceAdapter by lazy { RecommendPlaceAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        setupObserver()
    }

    private fun setupViews() {
        initRecommendPlaceAdapter()
        setupToolbarTitle()
        setupBackBtn()
        setupChipGroupEvent()
    }

    private fun initRecommendPlaceAdapter() {
        binding.recommendRvPlace.adapter = recommendPlaceAdapter
    }

    private fun setupToolbarTitle() {
        binding.recommendTvName.text = getString(R.string.recommend_place_top_bar).format(placeName)
    }

    private fun setupBackBtn() {
        binding.recommendIvBack.setOnClickListener { finish() }
    }

    private fun setupChipGroupEvent() {
        val chipGroup = binding.recommendChipGroup
        var lastCheckedChipId = chipGroup.checkedChipId

        viewModel.getRecommendPlace(placeX, placeY, placeName, "카페")
        (binding.recommendChipGroup[0] as Chip).typeface = Typeface.create(ResourcesCompat.getFont(this, R.font.pretendard_bold), Typeface.BOLD)

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                chipGroup.check(lastCheckedChipId)
                return@setOnCheckedStateChangeListener
            }

            if (checkedIds.first() == View.NO_ID) {
                chipGroup.check(lastCheckedChipId)
            } else {
                lastCheckedChipId = checkedIds.first()
                chipGroup.children.forEach { view ->
                    val chip = view as Chip
                    val isChecked = chip.id == checkedIds.first()
                    val fontId = if (isChecked) R.font.pretendard_bold else R.font.pretendard_regular
                    chip.typeface = Typeface.create(ResourcesCompat.getFont(this, fontId), if (isChecked) Typeface.BOLD else Typeface.NORMAL)
                }
            }
        }

        chipGroup.children.forEach { view ->
            view.setOnClickListener { chip ->
                viewModel.getRecommendPlace(placeX, placeY, placeName, (chip as Chip).text.toString())
            }
        }
    }


    private fun setupObserver() {
        viewModel.recommendPlaces.observe(this) { placeInfo ->
            recommendPlaceAdapter.submitList(placeInfo)
        }
    }
}

