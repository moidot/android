package com.moidot.moidot.presentation.util.deeplink

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import com.moidot.moidot.R
import com.moidot.moidot.presentation.ui.main.MainActivity
import com.moidot.moidot.presentation.ui.main.group.join.participate.view.ParticipateGroupActivity

enum class DeepLinkInfo(@StringRes val hostStringResId: Int) {

    MAIN(R.string.scheme_main) {
        override fun getIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    },

    KAKAO_INVITE(R.string.scheme_kakao_invite) {
        override fun getIntent(context: Context) =
            Intent(context, ParticipateGroupActivity::class.java)
    };

    abstract fun getIntent(context: Context): Intent
}