package br.com.fluo.fluo.services

import br.com.fluo.fluo.models.Project
import br.com.fluo.fluo.models.Task
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceProject {

    @GET("project")
    fun getProjects() : Call<List<Project>>

    @GET("project/{id}/tasks")
    fun getTasksProject(@Path("id") id: String): Call<List<Task>>

}