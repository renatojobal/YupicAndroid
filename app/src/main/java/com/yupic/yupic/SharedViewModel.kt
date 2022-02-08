package com.yupic.yupic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yupic.yupic.model.Project
import com.yupic.yupic.model.User
import com.yupic.yupic.repository.ApiInterface
import retrofit2.Call
import retrofit2.Callback

import retrofit2.Response
import timber.log.Timber


class SharedViewModel : ViewModel() {


    private val _projectsList : MutableLiveData<List<Project>> = MutableLiveData(listOf())
    val projectsList: LiveData<List<Project>> = _projectsList

    private val _user : MutableLiveData<User?> = MutableLiveData(null)
    val user : LiveData<User?> = _user
    fun loginUser(user: User){
        this._user.value = user
    }
    fun logoutUser(){
        this._user.value = null
    }

    private fun loadProjectList(){

        val apiInterface = ApiInterface.create().getProjects()

        apiInterface.enqueue(object : Callback<List<Project>>{
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                if(response.code() == 200){
                    response.body()?.let { responseBody ->
                        _projectsList.postValue(responseBody)
                    }
                }else{
                    Timber.e("Error getting projects. Response code: ${response.code()} \n Response: $response")
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                Timber.e(t, "Failure getting projects")
            }

        })

    }

    init {
        this.loadProjectList()
    }


}