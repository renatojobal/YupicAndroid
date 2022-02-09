package com.yupic.yupic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yupic.yupic.model.Node
import com.yupic.yupic.model.Option
import com.yupic.yupic.model.Project
import com.yupic.yupic.model.User
import com.yupic.yupic.repository.ApiInterface
import com.yupic.yupic.ui.NODE_TYPE_MULTIPLE_CHOICE
import com.yupic.yupic.ui.NODE_TYPE_NUMBER
import retrofit2.Call
import retrofit2.Callback

import retrofit2.Response
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SharedViewModel(
    private val database: FirebaseFirestore = Firebase.firestore
) : ViewModel() {


    private val _projectsList : MutableLiveData<List<Project>> = MutableLiveData(listOf())
    val projectsList: LiveData<List<Project>> = _projectsList

    /**
     * User
     */
    private val _user : MutableLiveData<User?> = MutableLiveData(null)
    val user : LiveData<User?> = _user
    fun loginUser(user: User){
        this._user.value = user
    }
    fun logoutUser(){
        this._user.value = null
    }

    /**
     * Form
     */
    private val _formNodes : MutableLiveData<List<Node>?> = MutableLiveData(null)
    val formNodes : LiveData<List<Node>?> = _formNodes

    /**
     * Load form data
     */
    private fun loadFormNodes(){
        database.collection("nodes")
            .get()
            .addOnSuccessListener { result ->
                val nodes = mutableListOf<Node>()
                Timber.i("Got result: ${result.documents}")
                result.documents.forEach { docSnapshot ->
                    Timber.d("Document: ${docSnapshot.data}")
                    val node = Node(
                        title = docSnapshot.getString("title") ?: "",
                    subtitle= docSnapshot.getString("subtitle") ?: "",
                    thumbnail= docSnapshot.getString("thumbnail"),
                     response= docSnapshot.getDouble("response"),
                     factor = docSnapshot.getDouble("factor") ?: 0.0,
                     type= docSnapshot.getString("type") ?: "",
                     result = docSnapshot.getDouble("result") ?: 0.0
                    )

                    // Add options if type is multiple choice
                    if(node.type == NODE_TYPE_MULTIPLE_CHOICE){
                        val optionsData = docSnapshot.get("options") as ArrayList<HashMap<String, *>>?
                        val options = mutableListOf<Option>()
                        optionsData?.forEach {
                            val dummyOption = Option(it)
                        }

                    }


                    nodes.add(node)
                }

                _formNodes.value = nodes.toList()


            }
            .addOnFailureListener {
                Timber.e(it, "Failed getting form questions")
            }
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
        this.loadFormNodes()
    }


}