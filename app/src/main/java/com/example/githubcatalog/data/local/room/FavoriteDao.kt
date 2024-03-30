package com.example.githubcatalog.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubcatalog.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE username = :username")
    suspend fun delete(username: String)

    @Query("SELECT * FROM favorites ORDER BY date_added DESC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteEntity>
}