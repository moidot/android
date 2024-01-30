package com.moidot.moidot.presentation.util.deeplink

import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moidot.moidot.presentation.ui.main.MainActivity
import com.moidot.moidot.presentation.util.Constant.GROUP_ID
import com.moidot.moidot.presentation.util.Constant.GROUP_NAME

class SchemeActivity : AppCompatActivity() {

    // TODO FCM 알림도 enum class 로 관리 예정
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink()
    }

    private fun handleDeepLink() {
        val deepLinkUrl = intent.data
        val deepLinkIntent = getSchemeIntent(deepLinkUrl.toString())
        if (isTaskRoot) { // 이미 앱이 실행중 -> 사용자가 앱을 실행한 상태에서 들어왔을 때
            TaskStackBuilder.create(this).apply {
                if (needAddMainForParent(deepLinkIntent)) { // 부모 activity로 Main 액티비티를 띠워야할 때
                    addNextIntentWithParentStack(DeepLinkInfo.MAIN.getIntent(this@SchemeActivity))
                }
                addNextIntent(deepLinkIntent)
            }.startActivities()
        } else {
            startActivity(deepLinkIntent)
        }
        finish()
    }

    private fun getSchemeIntent(deepLinkUrlStr: String?): Intent {
        if (deepLinkUrlStr == null) return DeepLinkInfo.MAIN.getIntent(this) // 예기치 못한 오류 대비 MainActivity로 보내기
        val params = getParams(deepLinkUrlStr)

        if (deepLinkUrlStr.startsWith("kakao")) { // TODO scheme 이 어떻게 되느냐에 따라 변경될 예정
            return DeepLinkInfo.KAKAO_INVITE.getIntent(this).apply {
                putExtra(GROUP_ID, params["groupId"]?.toInt())
                putExtra(GROUP_NAME, params["groupName"])
            }
        }

        return DeepLinkInfo.MAIN.getIntent(this)
    }

    /** parent로 MainActivity가 필요한 경우 분기처리
     *  MainActivity 자체를 요청하는 scheme에 대비하여 작성되었다.
     * */
    private fun needAddMainForParent(intent: Intent): Boolean =
        when (intent.component?.className) {
            MainActivity::class.java.name -> false
            else -> true
        }

    private fun getParams(uri:String?): Map<String, String> {
        val param = uri?.substring(uri.indexOf("?") + 1, uri.length)
        val result = HashMap<String, String>()
        param?.let {
            val queryList = it.split("&")
            queryList.forEach { query ->
                val string = query.split("=")
                runCatching { result[string[0]] = string[1] }
            }
        }
        return result
    }
}
