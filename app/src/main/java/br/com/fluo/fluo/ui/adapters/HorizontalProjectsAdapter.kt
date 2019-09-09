package br.com.fluo.fluo.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import br.com.fluo.fluo.R
import br.com.fluo.fluo.models.Project
import kotlinx.android.synthetic.main.item_horizontal_project.view.*

class HorizontalProjectsAdapter(var context: Context, var list: List<Project>) :
    RecyclerView.Adapter<HorizontalProjectsAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProjectViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_project, p0, false)
        return ProjectViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ProjectViewHolder, p1: Int) {
        p0.bind(context, list[p1], p1)
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name = itemView.name as Button

        fun bind(context: Context, item: Project, position: Int) {

            name.text = item.name

            name.setOnClickListener {

                name.background = context.resources.getDrawable(R.drawable.buttongreensolid, null)
                name.setTextColor(context.resources.getColor(R.color.white, null))

            }

        }

    }

}