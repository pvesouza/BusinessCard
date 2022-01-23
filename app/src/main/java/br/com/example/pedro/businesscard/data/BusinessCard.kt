package br.com.example.pedro.businesscard.data

import android.media.Image
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BusinessCard (
    @PrimaryKey (autoGenerate = true) val id:Int = 0,
    val name:String,
    val company:String,
    val email:String,
    val phone:String,
    val colorBackground:String
        ){
}