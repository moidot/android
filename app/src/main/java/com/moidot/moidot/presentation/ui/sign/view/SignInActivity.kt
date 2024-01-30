package com.moidot.moidot.presentation.ui.sign.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.moidot.moidot.BuildConfig.NAVER_CLIENT_ID
import com.moidot.moidot.BuildConfig.NAVER_CLIENT_SECRET_KEY
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivitySignInBinding
import com.moidot.moidot.presentation.ui.base.BaseActivity
import com.moidot.moidot.presentation.ui.main.MainActivity
import com.moidot.moidot.presentation.ui.sign.model.Platform.KAKAO
import com.moidot.moidot.presentation.ui.sign.model.Platform.NAVER
import com.moidot.moidot.presentation.ui.sign.viewmodel.SignInViewModel
import com.moidot.moidot.presentation.util.Constant.SCHEME_URL_STRING
import com.moidot.moidot.presentation.util.deeplink.SchemeActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val schemeUrl by lazy { intent.getStringExtra(SCHEME_URL_STRING) }
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initSdk()
        setupObserver()
        Log.d("kite", "로그인: " + schemeUrl)
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initSdk() {
        NaverIdLoginSDK.initialize(this, NAVER_CLIENT_ID, NAVER_CLIENT_SECRET_KEY, getString(R.string.app_name))
    }

    /**
     * scheme 을 통해 들어온 유저들은 로그인 후 해당화면으로 바로 이동한다.
     * [SchemeActivity]에서 바로 로그인 후 해당 화면으로 이동하는 로직이 구현되어 있습니다.
     */
    private fun setupObserver() {
        viewModel.loginSuccessState.observe(this) {
            if (it) {
                if (schemeUrl != null) moveToScheme()
                else moveToHome()
            }
        }
    }

    private fun moveToScheme() {
        Intent(this, SchemeActivity::class.java).apply {
            Log.d("kite", "로그인: " + schemeUrl)
            putExtra(SCHEME_URL_STRING, schemeUrl)
            startActivity(this)
        }
        finish()
    }

    private fun moveToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun onclickKakaoSignIn() { // 카카오 로그인
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

    fun onclickNaverSignIn() { // 네이버 로그인
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() = viewModel.signInWithSocialToken(NaverIdLoginSDK.getAccessToken().toString(), NAVER.name)
            override fun onFailure(httpStatus: Int, message: String) {}
            override fun onError(errorCode: Int, message: String) = onFailure(errorCode, message)
        }
        NaverIdLoginSDK.authenticate(this@SignInActivity, oauthLoginCallback)
    }

}