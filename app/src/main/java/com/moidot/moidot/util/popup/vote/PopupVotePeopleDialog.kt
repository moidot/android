package com.moidot.moidot.util.popup.vote

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import com.moidot.moidot.R
import com.moidot.moidot.databinding.PopupVotePeopleDialogBinding
import com.moidot.moidot.util.SpannableTxt
import com.moidot.moidot.util.view.dpToPx

class PopupVotePeopleDialog(
    context: Context,
    private val location: String,
    private val leaderName: String,
    private val people: List<String>,
) : Dialog(context, R.style.custom_dialog) {
    private lateinit var binding: PopupVotePeopleDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initAdapter()
    }

    private fun initBinding() {
        binding = PopupVotePeopleDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initView() {
        initDialogWindowView()
        initCloseBtnEvent()
        setTitleLocationTxt()
        setContentCntTxt()
    }

    private fun initDialogWindowView() {
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            decorView.setPadding(0, 0, 0, 30.dpToPx(context))
        }
    }

    private fun initCloseBtnEvent() {
        binding.popupVotePeopleIvClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setTitleLocationTxt() {
        val content = context.resources.getString(R.string.popup_vote_people_title).format(location)
        val spannableTxt = SpannableTxt(context).applySpannableStyles(content = content, target = location, styleResId = R.style.b1_bold_16)
        binding.popupVotePeopleTvTitle.text = spannableTxt
    }

    private fun setContentCntTxt() {
        binding.popupVotePeopleTvContent.text = context.resources.getString(R.string.popup_vote_people_content).format(people.size)
    }

    private fun initAdapter() {
        val votePeopleAdapter = VotePeopleAdapter(leaderName, people)
        binding.popupVotePeopleRvPeople.adapter = votePeopleAdapter
    }
}