package com.moidot.moidot.presentation.util

import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.ItemInfo
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.Social

val defaultFeed = FeedTemplate(
    content = Content(
        title = "오늘의 디저트",
        description = "#케익 #딸기 #삼평동 #카페 #분위기 #소개팅",
        imageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
        link = Link(
            webUrl = "https://developers.kakao.com",
            mobileWebUrl = "https://developers.kakao.com"
        )
    ),
    itemContent = ItemContent(
        profileText = "Kakao",
        profileImageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
        titleImageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
        titleImageText = "Cheese cake",
        titleImageCategory = "Cake",
        items = listOf(
            ItemInfo(item = "cake1", itemOp = "1000원"),
            ItemInfo(item = "cake2", itemOp = "2000원"),
            ItemInfo(item = "cake3", itemOp = "3000원"),
            ItemInfo(item = "cake4", itemOp = "4000원"),
            ItemInfo(item = "cake5", itemOp = "5000원")
        ),
        sum = "Total",
        sumOp = "15000원"
    ),
    social = Social(
        likeCount = 286,
        commentCount = 45,
        sharedCount = 845
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
                androidExecutionParams = mapOf("key1" to "value1", "key2" to "value2"),
                iosExecutionParams = mapOf("key1" to "value1", "key2" to "value2")
            )
        )
    )
)