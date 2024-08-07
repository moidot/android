package com.moidot.moidot.util.deeplink

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import com.moidot.moidot.R
import com.moidot.moidot.presentation.main.MainActivity
import com.moidot.moidot.presentation.main.group.join.participate.view.ParticipateGroupActivity
import com.moidot.moidot.presentation.sign.view.SignInActivity

enum class DeepLinkInfo(@StringRes val hostStringResId: Int) {

    SIGN_IN(R.string.scheme_sign_in) {
        override fun getIntent(context: Context) =
            Intent(context, SignInActivity::class.java)
    },

    MAIN(R.string.scheme_main) {
        override fun getIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    },

    INVITE(R.string.scheme_invite) {
        override fun getIntent(context: Context) =
            Intent(context, ParticipateGroupActivity::class.java)
    };

    abstract fun getIntent(context: Context): Intent
}