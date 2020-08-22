package com.jbhuiyan.projects.employeeportal.model.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.jbhuiyan.projects.employeeportal.model.database.EmployeeDAO
import com.jbhuiyan.projects.employeeportal.model.database.EmployeeRoomDB
import com.jbhuiyan.projects.employeeportal.model.models.Employee

class EmployeeRepository private constructor(application: Application) {

    private val employeeDAO: EmployeeDAO = EmployeeRoomDB.getDatabase(application).getEmployeeDao()

    fun insertEmployee(employee: Employee) {
        employeeDAO.insertEmployee(employee)
    }

    suspend fun deleteEmployee(employee: Employee) {
        employeeDAO.deleteEmployee(employee)
    }

    suspend fun getAllEmployeesLiveData(): LiveData<List<Employee>> {
        return employeeDAO.getAllEmployeesLiveData()
    }

    suspend fun getEmployeeByIdLiveData(employeeId: Int): LiveData<Employee?> {
        return employeeDAO.getEmployeeByIdLiveData(employeeId)
    }

    companion object {

        private var INSTANCE: EmployeeRepository? = null

        fun getInstance(application: Application): EmployeeRepository = INSTANCE ?: kotlin.run {
            INSTANCE = EmployeeRepository(application = application)
            INSTANCE!!
        }
    }
}
