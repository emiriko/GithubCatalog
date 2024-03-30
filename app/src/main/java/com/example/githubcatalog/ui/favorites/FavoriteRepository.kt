package com.example.githubcatalog.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubcatalog.data.Result
import com.example.githubcatalog.data.local.entity.FavoriteEntity
import com.example.githubcatalog.data.local.room.FavoriteDao

class FavoriteRepository private constructor(
    private val favoriteDao: FavoriteDao,
) {
//    fun getAllFavoriteUser(): LiveData<List<FavoriteEntity>> {
//        return favoriteDao.getAllFavoriteUser()
//    }

    fun getAllFavoriteUser(): LiveData<Result<List<FavoriteEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val localData: LiveData<Result<List<FavoriteEntity>>> =
                favoriteDao.getAllFavoriteUser().map { Result.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteEntity> {
        return favoriteDao.getFavoriteUserByUsername(username)
    }

    suspend fun addToFavorite(favorite: FavoriteEntity) {
        favoriteDao.insert(favorite)
    }

    suspend fun removeFromFavorite(username: String) {
        favoriteDao.delete(username)
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(
            newsDao: FavoriteDao,
        ): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(newsDao)
            }.also { instance = it }
    }
}