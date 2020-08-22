package com.jbhuiyan.projects.employeeportal.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.jbhuiyan.projects.employeeportal.R
import com.jbhuiyan.projects.employeeportal.model.models.Employee
import com.jbhuiyan.projects.employeeportal.utils.*
import com.jbhuiyan.projects.employeeportal.viewmodel.EmployeeViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import java.nio.charset.StandardCharsets

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val viewModel by viewModels<EmployeeViewModel>()
    var employeeId: Int = 0
    lateinit var selectedRoles:BooleanArray
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val departments = resources.getStringArray(R.array.departments)
        arguments?.let { bundle ->
            loadEmployeFromBundleData(bundle, departments)
        }
        initializeDepartmentSpinner(departments)
        initializeSaveButton()
        initializeRolesButton()
    }

    private fun initializeRolesButton() {
        buttonRoles.setOnClickListener {
            val sections = resources.getStringArray(R.array.roles)
            if(!::selectedRoles.isInitialized){
                selectedRoles= BooleanArray(sections.size)
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(R.string.settings_customize_dialog)
                    .setMultiChoiceItems(sections, selectedRoles) { dialogInterface, i, b ->
                        selectedRoles[i] = b
                    }
                    .setPositiveButton(android.R.string.ok) { dialogInterface, i ->

                    }
            builder.create().show()
        }
    }

    private fun initializeDepartmentSpinner(departments: Array<out String>) {
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, departments)
        spinnerDepartment.adapter = adapter
    }

    private fun initializeSaveButton() {
        buttonSave.setOnClickListener {
            if (validateUI()) {
                insertEmployee()
                findNavController().navigate(R.id.action_detailFragment_to_startFragment)
            }
        }
    }

    private fun loadEmployeFromBundleData(bundle: Bundle, departments: Array<out String>): Observer<Employee?> {
        return viewModel.employeeLiveData(bundle.getInt(EMPLOYEE_ID)).observe(viewLifecycleOwner) { employeeOrNull ->
            employeeOrNull?.let {
                employeeId = it.id
                titEmployeeFirstName.setText(it.firstName)
                titEmployeeLastName.setText(it.lastName)
                titEmployeeEmail.setText(it.email)
                titEmployeeUserName.setText(it.userName)
                val decodePassword = decodePassword(it.userPassword)
                titEmployeePassword.setText(decodePassword)
                titEmployeeConfirmPassword.setText(decodePassword)
                spinnerDepartment.setSelection(departments.indexOf(it.department))
                selectedRoles = it.employeeRolesToListBoolean()
                titEmployeeUserName.isEnabled = false
                titEmployeeEmail.isEnabled = false
            }
        }
    }

    private fun decodePassword(it: String): String {
        val data: ByteArray = Base64.decode(it, DEFAULT)
        return String(data, StandardCharsets.UTF_8)
    }

    private fun validateUI(): Boolean {
        var isValid: Boolean
        when {
            titEmployeeFirstName.getString().isBlank()->{
                titEmployeeFirstName.error = resources.getString(R.string.first_name_required)
                isValid = false;
            }
            titEmployeeLastName.getString().isBlank()->{
                titEmployeeLastName.error = resources.getString(R.string.last_name_required)
                isValid = false;
            }
            !titEmployeeEmail.getString().isValidEmail() -> {
                titEmployeeEmail.error = resources.getString(R.string.email_required)
                isValid = false;
            }

            !viewModel.employeeListLiveData.isValidUserName(titEmployeeUserName.getString()) -> {
                titEmployeeUserName.error = resources.getString(R.string.valid_username_required)
                isValid = false;
            }

            !viewModel.employeeListLiveData.isValidUserEmail(titEmployeeEmail.getString()) -> {
                titEmployeeEmail.error =  resources.getString(R.string.email_required)
                isValid = false;
            }

            titEmployeePassword.getString() != titEmployeeConfirmPassword.getString() -> {
                titEmployeeConfirmPassword.error =  resources.getString(R.string.password_not_match)
                titEmployeePassword.error = resources.getString(R.string.password_not_match)
                isValid = false;
            }
            else -> {
                isValid = true
            }
        }
        return isValid;
    }

    private fun insertEmployee() {
        val employee = Employee(titEmployeeFirstName.getString(),
                titEmployeeLastName.getString(),
                titEmployeeEmail.getString(),
                titEmployeeUserName.getString(),
                encodeToString(titEmployeePassword.getString().toByteArray(), DEFAULT),
                spinnerDepartment.selectedItem.toString())
        employee.id = employeeId
        employee.listBooleanToEmployeeRoles(selectedRoles)
        viewModel.saveEmployee(employee)
    }
}