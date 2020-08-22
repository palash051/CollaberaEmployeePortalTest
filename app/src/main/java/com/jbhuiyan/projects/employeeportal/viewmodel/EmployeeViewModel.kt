package com.jbhuiyan.projects.employeeportal.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.jbhuiyan.projects.employeeportal.model.models.Employee
import com.jbhuiyan.projects.employeeportal.model.repositories.EmployeeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeeViewModel (application: Application) : AndroidViewModel(application) {
    private val employeeRepository = EmployeeRepository.getInstance(application)
    val loading = MutableLiveData<Boolean>()
    val employeeListLiveData: LiveData<List<Employee>> = liveData(Dispatchers.IO) {
        emitSource(employeeRepository.getAllEmployeesLiveData())
    }

    fun employeeLiveData(id: Int): LiveData<Employee?> = liveData(Dispatchers.IO) {
        emitSource(employeeRepository.getEmployeeByIdLiveData(id))
    }

    fun saveEmployee(employee:Employee)= viewModelScope.launch (Dispatchers.IO){ employeeRepository.insertEmployee(employee) }

    fun deleteEmployee(employee:Employee)= viewModelScope.launch (Dispatchers.IO){ employeeRepository.deleteEmployee(employee) }
}