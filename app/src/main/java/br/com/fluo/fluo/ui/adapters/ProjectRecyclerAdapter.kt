package br.com.fluo.fluo.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.fluo.fluo.R
import br.com.fluo.fluo.models.Project
import kotlinx.android.synthetic.main.item_project.view.*

class ProjectRecyclerAdapter(var context: Context, var list: List<Project>, var listener: ItemClickListener) :
    RecyclerView.Adapter<ProjectRecyclerAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProjectViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_project, p0, false)
        return ProjectViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ProjectViewHolder, p1: Int) {
        p0.bind(context, list[p1], p1, listener)
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name = itemView.name
        var tasks = itemView.quantityTasks

        fun bind(context: Context, item: Project, p: Int, listener: ItemClickListener) {

            name.text = item.name

            if (item.tasks == 1) {
                tasks.text = context.getString(R.string.project_task, item.tasks.toString())
            } else if (item.tasks > 1) {
                tasks.text = context.getString(R.string.project_tasks, item.tasks.toString())
            } else if (item.tasks == 0) {
                tasks.text = context.getString(R.string.project_notask)
            }

            itemView.setOnClickListener {
                listener?.let{
                    it.onItemClick(item, p)
                }
            }

        }

    }

    interface ItemClickListener{
        fun onItemClick(project: Project, position: Int)
    }

}