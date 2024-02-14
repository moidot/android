package com.moidot.moidot.presentation.main.group.space.common.edit.view
import android.os.Bundle
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityEditMyGroupInfoBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.util.popup.edit.PopupEditDoneDialog

class EditMyGroupInfoActivity : BaseActivity<ActivityEditMyGroupInfoBinding>(R.layout.activity_edit_my_group_info) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialog = PopupEditDoneDialog( "김말이튀김", "서울 성북구 보문로34다길 2", "자동차", {})
        dialog.show(supportFragmentManager, "ss")
    }
}