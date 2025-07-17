package com.example.klaro.domain.repository.interfaces

import com.example.klaro.domain.repository.Result

interface RegistrationRepository
{

    suspend fun registerWithEmail(firstName: String, lastName: String, email: String, password: String): Result<Unit>

    suspend fun registerWithGoogle(idToken: String): Result<Unit>
}