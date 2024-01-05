package com.moidot.moidot.presentation.ui.sign.view

import android.os.Bundle
import androidx.activity.viewModels
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivitySignInBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity
import com.moidot.moidot.presentation.ui.sign.model.Platform.KAKAO
import com.moidot.moidot.presentation.ui.sign.model.Platform.NAVER
import com.moidot.moidot.presentation.ui.sign.viewmodel.SignInViewModel
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
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
        if (token != null) viewModel.signInWithSocialToken(token.accessToken, KAKAO.name)
    }

    private fun signInWithKakaoTalk(apiClient: UserApiClient) {
        apiClient.loginWithKakaoTalk(this) { token, error ->
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) return@loginWithKakaoTalk
            if (token != null) viewModel.signInWithSocialToken(token.accessToken, KAKAO.name) else signInWithKakaoAccount(apiClient)
        }
    }

    private fun signInWithKakaoAccount(apiClient: UserApiClient) {
        apiClient.loginWithKakaoAccount(this, callback = kakaoAccountSignInCallBack)
    }

    fun onclickNaverSignIn() {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() = viewModel.signInWithSocialToken(NaverIdLoginSDK.getAccessToken().toString(), NAVER.name)
            override fun onFailure(httpStatus: Int, message: String) {}
            override fun onError(errorCode: Int, message: String) = onFailure(errorCode, message)
        }
        NaverIdLoginSDK.authenticate(this@SignInActivity, oauthLoginCallback)
    }

}