package com.moidot.moidot.util.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.util.Log
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
        val clip = ClipData.newPlainText("dynamicLink", clipText)
        clipboard.setPrimaryClip(clip)
    }
}