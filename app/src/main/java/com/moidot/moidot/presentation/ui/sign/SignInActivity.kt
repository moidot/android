package com.moidot.moidot.presentation.ui.sign

import android.os.Bundle
import androidx.activity.viewModels
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivitySignInBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val viewModel: SignInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding.activity = this
    }

    fun onclickKakaoSignIn() {
        UserApiClient.instance.apply {
            if (isKakaoTalkLoginAvailable(this@SignInActivity)) signInWithKakaoTalk(this) else signInWithKakaoAccount(this)
        }
    }

    private val kakaoAccountSignInCallBack: (OAuthToken?, Throwable?) -> Unit = { token, _ ->
        if (token != null) viewModel.signInWithKakao(token.accessToken)
    }

    private fun signInWithKakaoTalk(apiClient: UserApiClient) {
        apiClient.loginWithKakaoTalk(this) { token, error ->
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) return@loginWithKakaoTalk
            if (token != null) viewModel.signInWithKakao(token.accessToken) else signInWithKakaoAccount(apiClient)
        }
    }

    private fun signInWithKakaoAccount(apiClient: UserApiClient) {
        apiClient.loginWithKakaoAccount(this, callback = kakaoAccountSignInCallBack)
    }

}