package com.android.user.fragment.set

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.BaseFragment
import com.android.user.R
import kotlinx.android.synthetic.main.user_fragment_set.view.defaultListView

/**
 * created by jiangshide on 2019-08-17.
 * email:18311271399@163.com
 */

class EditProfileFragment:BaseFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return setTitleView(R.layout.user_fragment_editprofile)
    }
}