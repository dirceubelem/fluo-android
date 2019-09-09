package br.com.fluo.fluo.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import br.com.fluo.fluo.R
import br.com.fluo.fluo.models.Project
import br.com.fluo.fluo.services.RetrofitInitializer
import br.com.fluo.fluo.ui.activities.MainActivity
import br.com.fluo.fluo.ui.adapters.ProjectAdapter
import br.com.fluo.fluo.ui.adapters.ProjectRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_projects.*
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */
class ProjectsFragment : Fragment(), ProjectRecyclerAdapter.ItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_projects, container, false)

        getProjects()

        return view
    }

    fun getProjects() {

        var s = RetrofitInitializer().serviceProject()
        var call = s.getProjects()

        var context = context as MainActivity

        call.enqueue(object : retrofit2.Callback<List<Project>> {

            override fun onResponse(call: Call<List<Project>>?, response: Response<List<Project>>?) {

                response?.let {

                    if (it.code() == 200) {

                        gridProjects.layoutManager = GridLayoutManager(context, 2)
                        gridProjects.adapter = ProjectRecyclerAdapter(context, it.body(), this@ProjectsFragment)

//                        gridProjects.adapter = ProjectAdapter(context, it.body())

                    }

                }

            }

            override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
            }

        })


    }

    override fun onItemClick(project: Project, position: Int) {
        Toast.makeText((context as MainActivity), project.name, Toast.LENGTH_SHORT).show()

        (context as MainActivity).setFragment(TasksFragment.newInstance(project.id))
    }
}
