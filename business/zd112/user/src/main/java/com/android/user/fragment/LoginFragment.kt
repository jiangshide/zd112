package com.android.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.android.base.BaseFragment
import com.android.user.R
import com.android.user.vm.UserVM
import kotlinx.android.synthetic.main.user_fragment_login.loginIcon
import kotlinx.android.synthetic.main.user_fragment_login.loginName
import kotlinx.android.synthetic.main.user_fragment_login.loginPsw
import kotlinx.android.synthetic.main.user_fragment_login.loginSubmit
import kotlinx.android.synthetic.main.user_fragment_login.protocolTxt

/**
 * created by jiangshide on 2019-10-08.
 * email:18311271399@163.com
 */
class LoginFragment : BaseFragment() {

  lateinit var userVm: UserVM

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    userVm = ViewModelProviders.of(this)
        .get(UserVM::class.java)
    return setTitleView(R.layout.user_fragment_login)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    loginIcon.setOnClickListener {

    }

    val loginName = loginName.text
    val loginPsw = loginPsw.text
    loginSubmit.setOnClickListener {

    }
    protocolTxt.setOnClickListener {
      userVm.reg(loginName.toString(),loginPsw.toString())
    }
  }
}