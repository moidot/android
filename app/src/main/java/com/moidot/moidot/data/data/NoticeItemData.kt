package com.moidot.moidot.data.data

data class NoticeItemData(val date: String, val title: String, val content: String)

// TODO 추후 서버 통신으로 변경될 예정
val noticeList = listOf(
    NoticeItemData(
        date = "2024.06.07", title = "모이닷 앱 서비스 출시!", content = "안녕하세요, ‘우리가 만나는 지점, 모이닷’입니다.\n" +
                "모이닷 웹에 이어, 안드로이드 버전이 출시 되었습니다!\n\n" +
                "많은 관심과 사랑 부탁드리며, \n문의 및 건의사항은 아래의 소통창구로 전달 부탁드립니다." +
                "\n감사합니다.\n\n" +
                "모이닷 공식 인스타그램 : \nhttps://www.instagram.com/moi_dot" +
                "\n\n모이닷 기타 문의 및 건의사항 : \nhttps://linktr.ee/moidot"
    )
)