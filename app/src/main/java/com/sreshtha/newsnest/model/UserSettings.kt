package com.sreshtha.newsnest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val theme: String,
    val lang: String
) : Serializable