package com.example.aplicacion0102;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users ORDER BY id DESC")
    LiveData<List<User>> getAllUsers();
}