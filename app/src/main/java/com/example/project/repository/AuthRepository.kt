package com.example.project.repository

import com.example.project.model.Account
import com.example.project.model.RegisterRequest
import com.example.project.network.RetrofitClient

class AuthRepository {

    private val api = RetrofitClient.apiService

    suspend fun login(username: String, password: String) =
        api.login(
            Account(
                accountName = username,
                accountPassword = password,
                accountType = 0
            )
        )
}