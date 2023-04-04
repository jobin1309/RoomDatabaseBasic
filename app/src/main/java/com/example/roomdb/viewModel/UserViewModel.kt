package com.example.roomdb.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.roomdb.data.UserDatabase
import com.example.roomdb.repository.UserRepository
import com.example.roomdb.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>
    private val repository: UserRepository;


    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao);
        readAllData = repository.readAllData;
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user);

        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user);
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user);
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUser();
        }
    }

    fun getImage(userId: Int){
        viewModelScope.launch (Dispatchers.IO)  {
            repository.getImage(userId)
        }
    }

//

    fun resetId() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetId();
        }
    }


}