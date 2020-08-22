package com.jbhuiyan.projects.employeeportal.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee_table")
data class Employee(
        var firstName: String,
        var lastName: String,
        var email: String,
        var userName: String,
        var userPassword: String,
        var department: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var roles: String=""
}
