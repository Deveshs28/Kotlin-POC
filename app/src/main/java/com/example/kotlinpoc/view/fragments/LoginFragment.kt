package com.example.kotlinpoc.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kotlinpoc.MyApplication
import com.example.kotlinpoc.R
import com.example.kotlinpoc.utils.AppConstants
import com.example.kotlinpoc.view.activities.MainActivity
import com.example.kotlinpoc.viewmodel.LoginViewModel
import com.example.kotlinpoc.viewmodel.ViewModelFactory
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val viewModel: LoginViewModel by lazy {
        MyApplication.apiComponent.inject(this)
        ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setObserver()
    }

    private fun initUI() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity!!)
        loginBtn.setOnClickListener {
            logBtnClickEvent()
            viewModel.processLogin(
                userName.text.toString().trim(),
                password.text.toString().trim(),
                rememberMeCheckbox.isChecked
            )
        }

        userName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    viewModel.fetchPassword(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun logBtnClickEvent() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, AppConstants.BUTTON_CLICK_EVENT_ID)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Login Button Click")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun setObserver() {
        viewModel.success.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    logApiEvent(AppConstants.LOGIN_SUCCESS_ID, "Login Success")
                    activity!!.startActivity(Intent(activity!!, MainActivity::class.java))
                    activity!!.finish()
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    logApiEvent(AppConstants.LOGIN_ERROR_ID, "Login Error : "+viewModel.getErrorMessage().value!!)
                    Toast.makeText(
                        activity!!, viewModel.getErrorMessage().value, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        viewModel.getUserModel().observe(viewLifecycleOwner, Observer {
            it?.let {
                password.setText(it.password)
            }
        })
    }

    private fun logApiEvent(id:String, message:String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, message)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}