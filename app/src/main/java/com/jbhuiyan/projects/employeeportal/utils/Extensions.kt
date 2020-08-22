package com.jbhuiyan.projects.employeeportal.utils

import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.room.TypeConverter
import com.google.android.material.textfield.TextInputEditText
import com.jbhuiyan.projects.employeeportal.model.models.Employee
import java.util.*

@TypeConverter
fun Employee.listBooleanToEmployeeRoles(list: BooleanArray): String {
    this.roles = ""
    for (i in list) {
        if (i) this.roles += "T"
        else this.roles += "F"
    }
    return this.roles
}

@TypeConverter
fun Employee.employeeRolesToListBoolean(): BooleanArray {
    var list = BooleanArray(this.roles.length)
    var index=0
    this.roles.forEach { c ->
        if (c == 'F') list[index]=false
        else list[index]=true
        index++
    }
    return list
}

fun Employee.fullName():String{
    return this.lastName +", " + this.firstName
}

fun Employee.details():String{
    return this.userName +", " + this.email + ", " + this.department
}

fun String.isValidEmail(): Boolean
        = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.toUpperCaseEqual(compareTo:String):Boolean {
    return this.trim().toUpperCase(Locale.getDefault()) == compareTo.trim().toUpperCase(Locale.getDefault())
}

fun LiveData<List<Employee>>.isValidUserName(username:String):Boolean{
    if(username.isBlank()) return false
    if(this.value.isNullOrEmpty()) return true
    this.value!!.forEach{
        if(it.userName.toUpperCaseEqual(username))
            return false
    }
    return true
}

fun LiveData<List<Employee>>.isValidUserEmail(newEmail:String):Boolean{
    if(this.value.isNullOrEmpty()) return true
    this.value!!.forEach{
        if(it.email.toUpperCaseEqual(newEmail))
            return false
    }
    return true
}

fun TextInputEditText.getString():String{
    return this.text.toString()
}

