package com.moidot.moidot.presentation.util.share

import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link

class KakaoFeedSetting(private val groupId: Int, private val groupName: String) {
    val defaultFeed = FeedTemplate(
        content = Content(
            title = groupName,
            description = "초대장이 도착했어요!",
            imageUrl = "https://moidot-bucket.s3.ap-northeast-2.amazonaws.com/image/kakao-message/feed_%E1%84%8E%E1%85%A9%E1%84%83%E1%85%A2_png.png",
            link = Link(
                webUrl = "https://developers.kakao.com", // TODO 웹 서버 올라오면 등록하기
                mobileWebUrl = "https://play.google.com/store/apps/details?id=com.moidot.moidot"
            )
        ),
        buttons = listOf(
            Button(
                "웹으로 보기",
                Link(
                    webUrl = "https://developers.kakao.com",
                    mobileWebUrl = "https://developers.kakao.com"
                )
            ),
            Button(
                "앱으로 보기",
                Link(
                    androidExecutionParams = mapOf("groupId" to groupId.toString(), "groupName" to groupName),
                    iosExecutionParams = mapOf("groupId" to groupId.toString(), "groupName" to groupName)
                )
            )
        )
    )
}


