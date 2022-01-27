package br.com.example.pedro.businesscard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BusinessCard::class], version = 2)
abstract class AppDataBase : RoomDatabase() {

    abstract fun businessDao() : BusinessCardDao

    companion object{
        @Volatile
        private var INSTANCE:AppDataBase? = null

        fun getDataBase(context: Context) : AppDataBase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "businesseCard_db").fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }

        }
    }

}