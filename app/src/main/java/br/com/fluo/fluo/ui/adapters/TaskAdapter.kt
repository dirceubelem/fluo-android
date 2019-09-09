package br.com.fluo.fluo.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.fluo.fluo.R
import br.com.fluo.fluo.helper.DateTime
import br.com.fluo.fluo.models.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TaskAdapter(var context: Context, var list: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var listTasks: ArrayList<Task> = list as ArrayList<Task>

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TaskViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_task, p0, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: TaskViewHolder, p1: Int) {
        p0.bind(context, list[p1], p1)
    }

    fun removeAt(position: Int) {
        listTasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun reload() {
        notifyDataSetChanged()
    }

    fun filter(text: String) {

        var listFilter = ArrayList<Task>()

        listTasks.forEach {

            if (it.name.toLowerCase().contains(text.toLowerCase())) {
                listFilter.add(it)
            }

        }

        list = listFilter

        notifyDataSetChanged()

    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name = itemView.name
        var datetime = itemView.datetime

        fun bind(context: Context, item: Task, p: Int) {
            name.text = item.name

            var date = DateTime(item.createdAt, "dd/MM/yyyy HH:mm:ss")

            datetime.text = date.toString("HH:mm")
        }

    }

}