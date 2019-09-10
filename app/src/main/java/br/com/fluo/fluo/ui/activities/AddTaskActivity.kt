package br.com.fluo.fluo.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import br.com.fluo.fluo.R
import br.com.fluo.fluo.models.Project
import br.com.fluo.fluo.services.RetrofitInitializer
import br.com.fluo.fluo.ui.adapters.HorizontalProjectsAdapter
import kotlinx.android.synthetic.main.activity_add_task.*
import retrofit2.Call
import retrofit2.Response

class AddTaskActivity : AppCompatActivity(), HorizontalProjectsAdapter.ProjectViewHolderListener {

    lateinit var adapter: HorizontalProjectsAdapter
    lateinit var listprojects: List<Project>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        getProjects()
        closetask.setOnClickListener {
            finish()
        }
    }

    fun getProjects() {

        var s = RetrofitInitializer().serviceProject()
        var call = s.getProjects()

        call.enqueue(object : retrofit2.Callback<List<Project>> {

            override fun onResponse(call: Call<List<Project>>?, response: Response<List<Project>>?) {

                response?.let {

                    if (it.code() == 200) {

                        listprojects = it.body()

                        val layoutManager =
                            LinearLayoutManager(this@AddTaskActivity, LinearLayoutManager.HORIZONTAL, false)
                        projects.layoutManager = layoutManager

                        adapter = HorizontalProjectsAdapter(this@AddTaskActivity, listprojects, -1, this@AddTaskActivity)

                        projects.adapter = adapter

                    }

                }

            }

            override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
            }

        })


    }

    override fun selectedItem(project: Project, selected: Int) {

        projects.adapter = HorizontalProjectsAdapter(this, listprojects, selected, this)
        projects.scrollToPosition(selected)

    }
}
