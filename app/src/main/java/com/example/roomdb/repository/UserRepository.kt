package com.example.roomdb.repository

import android.app.Person
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import com.example.roomdb.data.UserDao
import com.example.roomdb.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao)

{

    val readAllData: LiveData<List<User>> = userDao.readAllData();


    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user);
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user);
    }

    suspend fun deleteAllUser() {
        userDao.deleteAllUsers()
    }

    fun resetId() {
        userDao.resetId();
    }

    fun getImage(userId: Int){
        userDao.getUser(userId);
    }



//    fun searchDatabase(searchQuery: String): Flow<List<User>> {
//        return userDao.searchDatabase("%$searchQuery%")
//    }


}