package com.moidot.moidot.util.share

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.moidot.moidot.R

object FirebaseLinkShareManger {
    fun shareLink(context: Context, groupId: Int, groupName: String) {
        val inviteLink = "https://moidot.page.link/invite?groupId=$groupId&groupName=$groupName"
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(inviteLink))
            .setDomainUriPrefix("https://moidot.page.link")
            .setAndroidParameters(
                AndroidParameters.Builder("com.moidot.moidot").build()
            )
            .buildShortDynamicLink()

        dynamicLink.addOnSuccessListener {
            copyToClipboard(context, groupName, it.shortLink.toString())
        }.addOnFailureListener {
            Log.e("error", it.toString())
        }
    }

    private fun copyToClipboard(context: Context, groupName: String, link: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipText = context.getString(R.string.link_invite_clip_text).format(groupName, link)

        val clipData = ClipData.newPlainText("dynamicLink", clipText).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) { // 13이상 버전에는 시스템 메세지가 이미 등장 (중복 방지)
                Toast.makeText(context, "클립보드에 복사가 완료되었습니다!", Toast.LENGTH_SHORT).show()
            }
        }

        clipboard.setPrimaryClip(clipData)
    }
}