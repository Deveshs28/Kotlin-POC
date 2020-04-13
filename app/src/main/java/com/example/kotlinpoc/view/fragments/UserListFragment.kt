package com.example.kotlinpoc.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinpoc.MyApplication
import com.example.kotlinpoc.R
import com.example.kotlinpoc.data.model.UserResultModel
import com.example.kotlinpoc.interfaces.RecyclerClickListener
import com.example.kotlinpoc.utils.AppConstants
import com.example.kotlinpoc.view.adapters.UserListAdapter
import com.example.kotlinpoc.viewmodel.ListViewModel
import com.example.kotlinpoc.viewmodel.ViewModelFactory
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_user_list_fragment.*
import javax.inject.Inject

class UserListFragment : Fragment(), RecyclerClickListener {
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var adapter: UserListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val userList = ArrayList<UserResultModel>()
    private var page: Int = 1

    private val viewModel: ListViewModel by lazy {
        MyApplication.apiComponent.inject(this)
        ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }

    companion object {
        fun newInstance(): UserListFragment {
            return UserListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
    }

    private fun initView() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity!!)
        userRecyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        userRecyclerView.layoutManager = linearLayoutManager
        adapter = UserListAdapter(userList, this)
        userRecyclerView.adapter = adapter
        viewModel.fetchUserList(page)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.fetchUserListBySearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun setObserver() {
        viewModel.getError().observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    logApiEvent(AppConstants.API_ERROR_ID, "Error : "+viewModel.getErrorMessage().value)
                    Toast.makeText(
                        activity!!,
                        viewModel.getErrorMessage().value,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModel.getUserList().observe(viewLifecycleOwner, Observer {
            it?.let {
                logApiEvent(AppConstants.API_SUCCESS_ID, "API Success")
                userList.clear()
                userList.addAll(it)
                adapter.updateList(userList)
            }
        })

        viewModel.getLoading().observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onClick(position: Int) {
    }

    private fun logApiEvent(id:String, message:String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, message)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}