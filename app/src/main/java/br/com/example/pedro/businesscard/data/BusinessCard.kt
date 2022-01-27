package br.com.example.pedro.businesscard.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BusinessCard (
    @PrimaryKey (autoGenerate = true) val id:Int = 0,
    val name:String,
    val company:String,
    val email:String,
    val phone:String,
    val colorBackground:String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val image:ByteArray? = null
    ){
}