package com.jbhuiyan.projects.employeeportal.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jbhuiyan.projects.employeeportal.model.models.Employee
import com.jbhuiyan.projects.employeeportal.utils.ROOM_DB_NAME

@Database(entities = [Employee::class], version = 1, exportSchema = false)
abstract class EmployeeRoomDB : RoomDatabase() {
    abstract fun getEmployeeDao(): EmployeeDAO

    companion object {
        private var INSTANCE: EmployeeRoomDB? = null

        fun getDatabase(context: Context) = INSTANCE ?: kotlin.run {
            Room.databaseBuilder(
                    context.applicationContext,
                    EmployeeRoomDB::class.java,
                    ROOM_DB_NAME
            )
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}