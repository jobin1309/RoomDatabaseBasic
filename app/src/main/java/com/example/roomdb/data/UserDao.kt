package com.example.roomdb.data

import android.app.Person
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.roomdb.model.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User);

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User);

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers();

    @Query("DELETE FROM sqlite_sequence WHERE name='user_table'")
    fun resetId();

    @Query("SELECT * FROM user_table WHERE id = :userId")
    fun getUser(userId: Int): User



//    @Query("SELECT * FROM person_table WHERE firstName LIKE :searchQuery OR lastName LIKE :searchQuery")
//    fun searchDatabase(searchQuery: String): Flow<List<User>>

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>


}