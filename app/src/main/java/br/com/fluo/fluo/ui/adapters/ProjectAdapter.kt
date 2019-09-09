package br.com.fluo.fluo.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.fluo.fluo.R
import br.com.fluo.fluo.models.Project

class ProjectAdapter(var context: Context, var list: List<Project>) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.item_project, null)

        var name = view.findViewById<TextView>(R.id.name)
        name.text = list[position].name

        return view
    }

    override fun getItem(position: Int): Any {
        return ""
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }


}