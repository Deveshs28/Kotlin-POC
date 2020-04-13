package com.example.kotlinpoc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.kotlinpoc.data.model.UserResultModel
import com.example.kotlinpoc.data.rest.ApiService
import com.example.kotlinpoc.repository.DatabaseRepository
import com.example.kotlinpoc.viewmodel.ListViewModel
import io.reactivex.Maybe
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.net.SocketException
import java.util.*

@RunWith(JUnit4::class)
class ListViewModelTest {
    @get:Rule
    public val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var apiService: ApiService
//    @Mock
//    lateinit var myApplication: MyApplication
    @Mock
    lateinit var databaseRepository: DatabaseRepository

    lateinit var listViewModel: ListViewModel

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        val myApplication:MyApplication = mock(MyApplication::class.java)
        this.listViewModel =ListViewModel(myApplication, apiService, databaseRepository)
    }

    @Test
    fun fetchUserList(){
        Mockito.`when`(this.apiService.fetchUserList(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer{
            return@thenAnswer Maybe.just(ArgumentMatchers.anyList<UserResultModel>())
        }
        val observer = mock(Observer::class.java) as Observer<List<UserResultModel>>
        this.listViewModel.userList.observeForever(observer)
        //Invoke
        this.listViewModel.fetchUserList(1)
        //Verify
        assertNotNull(this.listViewModel.userList.value)
        assertEquals(false, this.listViewModel.loadError)
    }

    @Test
    fun fetchUserListErrorScenario(){
        Mockito.`when`(this.apiService.fetchUserList(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer{
            return@thenAnswer Maybe.error<SocketException>(SocketException("No network error"))
        }

        val observer = mock(Observer::class.java) as Observer<Boolean>
        this.listViewModel.loadError.observeForever(observer)
        //Invoke
        this.listViewModel.fetchUserList(ArgumentMatchers.anyInt())
        //Verify
        assertEquals(true, this.listViewModel.loadError)
    }

}