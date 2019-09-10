package br.com.fluo.fluo.services

import br.com.fluo.fluo.models.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceTask {

    @POST("task")
    fun newTask(@Body task: Task): Call<Void>

}