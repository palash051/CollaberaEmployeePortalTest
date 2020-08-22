package com.jbhuiyan.projects.employeeportal.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jbhuiyan.projects.employeeportal.model.models.Employee

@Dao
interface EmployeeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployee(employee: Employee)

    @Delete
    fun deleteEmployee(employee: Employee)

    @Query("SELECT * FROM employee_table")
    fun getAllEmployeesLiveData(): LiveData<List<Employee>>

    @Query("SELECT * FROM employee_table WHERE id = :employeeId")
    fun getEmployeeByIdLiveData(employeeId: Int): LiveData<Employee?>
}