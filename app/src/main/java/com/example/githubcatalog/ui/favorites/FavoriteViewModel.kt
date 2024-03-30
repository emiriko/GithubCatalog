package com.example.githubcatalog.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubcatalog.data.local.entity.FavoriteEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    fun getAllFavoriteUser() = favoriteRepository.getAllFavoriteUser()

    fun getFavoriteUserByUsername(username: String) =
        favoriteRepository.getFavoriteUserByUsername(username)

    fun addToFavorite(favorite: FavoriteEntity) {
        viewModelScope.launch {
            favoriteRepository.addToFavorite(favorite)
        }
    }

    fun removeFromFavorite(username: String) {
        viewModelScope.launch {
            favoriteRepository.removeFromFavorite(username)
        }
    }
}