package com.example.githubcatalog.ui.di

import android.content.Context
import com.example.githubcatalog.data.local.room.FavoriteDatabase
import com.example.githubcatalog.ui.favorites.FavoriteRepository

object Injection {
    fun provideRepository(context: Context): FavoriteRepository {
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoriteDao()
        return FavoriteRepository.getInstance(dao)
    }
}