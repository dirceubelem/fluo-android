package br.com.fluo.fluo.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import br.com.fluo.fluo.R
import br.com.fluo.fluo.app.FluoApp
import br.com.fluo.fluo.helper.DateTime
import br.com.fluo.fluo.models.Project
import br.com.fluo.fluo.models.Task
import br.com.fluo.fluo.services.RetrofitInitializer
import br.com.fluo.fluo.ui.adapters.HorizontalProjectsAdapter
import kotlinx.android.synthetic.main.activity_add_task.*
import retrofit2.Call
import retrofit2.Response

class AddTaskActivity : AppCompatActivity(), HorizontalProjectsAdapter.ProjectViewHolderListener {

    lateinit var adapter: HorizontalProjectsAdapter
    lateinit var listprojects: List<Project>
    lateinit var dateTask: DateTime
    lateinit var project: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        getProjects()

        closetask.setOnClickListener {
            finish()
        }

        selectdate.setOnClickListener {
            showPickerDate()
        }

        add.setOnClickListener {
            saveTask()
        }
    }

    fun saveTask() {

        var s = RetrofitInitializer().serviceTask()

        var task = Task()
        task.name = name.text.toString()
        task.description = description.text.toString()
        task.idProject = project.id
        task.idAccountTo = FluoApp.account!!.id

        var call = s.newTask(task)

        call.enqueue(object : retrofit2.Callback<Void> {

            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {

                response?.let {
                    if(it.code() == 201){

                        Toast.makeText(this@AddTaskActivity, "Ok", Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(this@AddTaskActivity, "Error 201", Toast.LENGTH_LONG).show()
                    }
                }

            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {

                Toast.makeText(this@AddTaskActivity, "Error", Toast.LENGTH_LONG).show()

            }

        })

    }

    fun showPickerDate() {

        var now = DateTime.now()

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                dateTask = DateTime(year, month + 1, dayOfMonth)
//                Toast.makeText(this, dateTask.toString(), Toast.LENGTH_LONG).show()

                showPickerTime()

            }, now.year, now.month - 1, now.day
        )

        dpd.show()

    }

    fun showPickerTime() {

        var now = DateTime.now()

        val tpd = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                dateTask.addHour(hourOfDay.toLong())
                dateTask.addMinute(minute.toLong())

//                Toast.makeText(this, dateTask.toString(), Toast.LENGTH_LONG).show()

                selectdate.text = dateTask.toString("dd/MM/yyyy HH:mm")


            }, now.hour, now.minute, false
        )

        tpd.show()

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

                        adapter =
                            HorizontalProjectsAdapter(this@AddTaskActivity, listprojects, -1, this@AddTaskActivity)

                        projects.adapter = adapter

                    }

                }

            }

            override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
            }

        })


    }

    override fun selectedItem(project: Project, selected: Int) {

        this.project = project

        projects.adapter = HorizontalProjectsAdapter(this, listprojects, selected, this)
        projects.scrollToPosition(selected)

    }
}
