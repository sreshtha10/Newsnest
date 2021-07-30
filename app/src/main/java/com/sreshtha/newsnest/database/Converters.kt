package com.sreshtha.newsnest.database

import androidx.room.TypeConverter
import com.sreshtha.newsnest.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }
}