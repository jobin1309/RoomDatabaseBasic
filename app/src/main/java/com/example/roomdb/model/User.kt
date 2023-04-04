package com.example.roomdb.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: ByteArray?,
    val firstName: String,
    @ColumnInfo(name = "location") // Renamed column
    val lastName: String,
    val age: Int,

): Parcelable {

}