package com.moidot.moidot.presentation.main.group.space.leader.vote.progress

import android.os.Bundle
import android.view.View
import com.moidot.moidot.R
import com.moidot.moidot.databinding.FragmentLeaderVoteProgressBinding
import com.moidot.moidot.presentation.base.BaseFragment
import com.moidot.moidot.util.Constant.CRATE_VOTE_MSG_EXTRA
import com.moidot.moidot.util.Constant.CRATE_VOTE_SUCCESS_STATE
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.CustomSnackBar

class LeaderVoteProgressFragment : BaseFragment<FragmentLeaderVoteProgressBinding>(R.layout.fragment_leader_vote_progress) {

    private val groupId by lazy { arguments?.getInt(GROUP_ID) ?: -1 }
    private val voteCreateState by lazy { arguments?.getBoolean(CRATE_VOTE_SUCCESS_STATE) ?: false }
    private val voteCreateSnackBarMsg by lazy { arguments?.getString(CRATE_VOTE_MSG_EXTRA) ?: getString(R.string.create_vote_end_time_msg_vote_start) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSnackBarAfterVoteCreation()
    }

    private fun showSnackBarAfterVoteCreation() {
        if (voteCreateState) CustomSnackBar.makeSnackBar(binding.root, voteCreateSnackBarMsg).show()
    }

}