package com.yupic.yupic.repository

import com.yupic.yupic.model.Project
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiInterface {

    @GET("core/projects/")
    fun getProjects() : Call<List<Project>>


    companion object {

        const val ENTERPRISES_URL = "http://10.0.2.2:8000/api/"

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ENTERPRISES_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }


}