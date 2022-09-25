package com.bbj.kinono.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

//@Database(entities = [])
abstract class MovieDatabase() : RoomDatabase() {

    abstract fun getDAO()

}