package br.com.fluo.fluo.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import br.com.fluo.fluo.R
import br.com.fluo.fluo.helper.SwipeRecyclerView
import br.com.fluo.fluo.models.Task
import br.com.fluo.fluo.services.RetrofitInitializer
import br.com.fluo.fluo.ui.activities.MainActivity
import br.com.fluo.fluo.ui.adapters.TaskAdapter
import kotlinx.android.synthetic.main.fragment_tasks.*
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */
class TasksFragment : Fragment(), SwipeRecyclerView.Companion.SwipeRecyclerViewListener {

    var idProject: String = ""
    lateinit var adapter: TaskAdapter

    companion object {

        fun newInstance(idProject: String): TasksFragment {
            var t = TasksFragment()
            t.idProject = idProject
            return t
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_tasks, container, false)

        getTasks()

        var search = view.findViewById<SearchView>(R.id.search)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(text: String?): Boolean {

                adapter.filter(text!!)

                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {

                adapter.filter(text!!)

                return false
            }
        })

        return view
    }

    fun getTasks() {

        var s = RetrofitInitializer().serviceProject()
        var call = s.getTasksProject(idProject)

        var context = context as MainActivity

        call.enqueue(object : retrofit2.Callback<List<Task>> {

            override fun onResponse(call: Call<List<Task>>?, response: Response<List<Task>>?) {

                response?.let {

                    if (it.code() == 200) {

                        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        adapter = TaskAdapter(context, it.body())
                        tasks.adapter = adapter
                        tasks.layoutManager = layoutManager

                        SwipeRecyclerView.createSwipe(context, tasks, this@TasksFragment)

                    }

                }

            }

            override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {

            }

        })


    }

    override fun removeSelectedItem(position: Int) {
        adapter.removeAt(position)

        // TODO: implementar chamada na API para excluir a tarefa
    }

    override fun reload() {
        adapter.reload()
    }
}
