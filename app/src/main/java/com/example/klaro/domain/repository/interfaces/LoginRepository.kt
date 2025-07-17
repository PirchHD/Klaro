package com.example.klaro.domain.repository.interfaces

import com.example.klaro.domain.repository.Result
import com.google.firebase.auth.FirebaseUser

interface LoginRepository
{
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun loginWithGoogle(idToken: String): Result<FirebaseUser>
}