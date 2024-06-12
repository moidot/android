package com.moidot.moidot.presentation.main.group.space.common.edit.view
import android.os.Bundle
import com.moidot.moidot.R
import com.moidot.moidot.databinding.ActivityEditMyGroupInfoBinding
import com.moidot.moidot.presentation.base.BaseActivity
import com.moidot.moidot.util.Constant.GROUP_ID
import com.moidot.moidot.util.Constant.GROUP_NAME
import com.moidot.moidot.util.popup.edit.PopupEditDoneDialog

class EditMyGroupInfoActivity : BaseActivity<ActivityEditMyGroupInfoBinding>(R.layout.activity_edit_my_group_info) {

    private val groupId by lazy { intent.getIntExtra(GROUP_ID, -1) }
    private val groupName by lazy { intent.getIntExtra(GROUP_NAME, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun initBinding() {

    }

    private fun showEditPopupDialog() {
        val dialog = PopupEditDoneDialog( "김말이튀김", "서울 성북구 보문로34다길 2", "자동차", {})
        dialog.show(supportFragmentManager, "ss")
    }
}