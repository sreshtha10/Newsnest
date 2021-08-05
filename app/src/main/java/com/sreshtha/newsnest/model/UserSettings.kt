package com.sreshtha.newsnest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey(autoGenerate = false)
    val id:Int = 1,
    val theme:String
)