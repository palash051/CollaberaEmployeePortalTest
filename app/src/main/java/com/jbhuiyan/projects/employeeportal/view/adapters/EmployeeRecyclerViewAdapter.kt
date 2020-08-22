package com.jbhuiyan.projects.employeeportal.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jbhuiyan.projects.employeeportal.R
import com.jbhuiyan.projects.employeeportal.model.models.Employee
import com.jbhuiyan.projects.employeeportal.utils.details
import com.jbhuiyan.projects.employeeportal.utils.fullName


class EmployeeRecyclerViewAdapter(private val onClickListener: (Employee) -> Unit) :
    ListAdapter<Employee, EmployeeViewHolder>(EmployeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_employee, parent, false)
        return EmployeeViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.item = getItem(position)
    }

    fun getAdapterItemByPosition(position:Int):Employee{
        return getItem(position)

    }
}

class EmployeeViewHolder(private val view: View, private val onClickListener: (Employee) -> Unit) :
    RecyclerView.ViewHolder(view) {

    var item: Employee? = null
        set(value) {
            value?.let { newValue ->
                field = newValue
                view.setOnClickListener { onClickListener(newValue) }
                view.findViewById<TextView>(R.id.textViewName).text = newValue.fullName()
                view.findViewById<TextView>(R.id.textViewEmail).text = newValue.details()
            }
        }
}

class EmployeeDiffCallback : DiffUtil.ItemCallback<Employee>() {

    override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
        return oldItem == newItem
    }
}
