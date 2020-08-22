package com.jbhuiyan.projects.employeeportal.view.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jbhuiyan.projects.employeeportal.R
import com.jbhuiyan.projects.employeeportal.utils.EMPLOYEE_ID
import com.jbhuiyan.projects.employeeportal.utils.EMPLOYEE_NAME
import com.jbhuiyan.projects.employeeportal.utils.fullName
import com.jbhuiyan.projects.employeeportal.view.adapters.EmployeeRecyclerViewAdapter
import com.jbhuiyan.projects.employeeportal.viewmodel.EmployeeViewModel
import kotlinx.android.synthetic.main.fragment_start.*


class StartFragment : Fragment(R.layout.fragment_start) {

    private val viewModel by viewModels<EmployeeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAddButton()
        viewModel.loading.value=true
        setupRecyclerView()
        viewModelInitialization()
    }

    private fun viewModelInitialization() {
        viewModel.employeeListLiveData.observe(viewLifecycleOwner) { employeeList ->
            viewModel.loading.value = false
            (rvEmployeeList.adapter as EmployeeRecyclerViewAdapter).submitList(employeeList)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            cardViewListLoading.visibility = when {
                isLoading -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun initAddButton() {
        fabAddEmployee.setOnClickListener {
            val bundle = bundleOf(EMPLOYEE_ID to 0, EMPLOYEE_NAME to getString(R.string.add_new))
            findNavController().navigate(R.id.action_startFragment_to_detailFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        rvEmployeeList.apply {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = EmployeeRecyclerViewAdapter { employee ->
                val bundle = bundleOf(EMPLOYEE_ID to employee.id, EMPLOYEE_NAME to employee.fullName())
                findNavController().navigate(R.id.action_startFragment_to_detailFragment, bundle)
            }
            this.addItemDecoration(
                    DividerItemDecoration(
                            this.context,
                            (this.layoutManager as LinearLayoutManager).orientation
                    )
            )
        }

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                var employee = (rvEmployeeList.adapter as EmployeeRecyclerViewAdapter).getAdapterItemByPosition(position)
                viewModel.deleteEmployee(employee)

            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rvEmployeeList)
    }
}