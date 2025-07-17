package com.example.klaro.domain.repository.implementations

import com.example.klaro.domain.model.ModelCollections
import com.example.klaro.domain.repository.Result
import com.example.klaro.domain.repository.interfaces.RegistrationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : RegistrationRepository {


    override suspend fun registerWithEmail(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<Unit> = suspendCancellableCoroutine { cont ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener { e -> cont.resume(Result.Error(e.toString()), null) }
            .addOnSuccessListener { result ->
                val user = result.user

                if (user == null)
                {
                    cont.resume(Result.Error("User is null"), null)
                    return@addOnSuccessListener
                }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName("$firstName $lastName")
                    .build()

                user.updateProfile(profileUpdates)
                    .addOnCompleteListener {taskProfile ->
                        if (!taskProfile.isSuccessful)
                        {
                            cont.resume(Result.Error(taskProfile.exception.toString()), null)
                            return@addOnCompleteListener
                        }

                        val uid = user.uid
                        val userData = mapOf(
                            "firstName" to firstName,
                            "lastName"  to lastName,
                            "email"     to email,
                            "createdAt" to FieldValue.serverTimestamp()
                        )

                        firestore.collection(ModelCollections.USER_DATA)
                            .document(uid)
                            .set(userData)
                            .addOnSuccessListener { cont.resume(Result.Success(Unit), null) }
                            .addOnFailureListener { e ->  cont.resume(Result.Error(e.toString()), null) }
                    }
            }
    }

    override suspend fun registerWithGoogle(idToken: String): Result<Unit> = suspendCancellableCoroutine { cont ->
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnFailureListener { e -> cont.resume(Result.Error(e.toString()), null) }
            .addOnSuccessListener { result ->
                val user = result.user

                if (user == null)
                {
                    cont.resume(Result.Error("User is null"), null)
                    return@addOnSuccessListener
                }

                val displayName = user.displayName ?: ""
                val nameParts = displayName.split(" ")
                val firstName = nameParts.firstOrNull() ?: ""
                val lastName = if (nameParts.size > 1) nameParts.subList(1, nameParts.size).joinToString(" ") else ""

                val userData = mapOf(
                    "firstName" to firstName,
                    "lastName"  to lastName,
                    "email"     to (user.email ?: ""),
                    "createdAt" to FieldValue.serverTimestamp()
                )

                firestore.collection("users")
                    .document(user.uid)
                    .set(userData)
                    .addOnSuccessListener { cont.resume(Result.Success(Unit), null) }
                    .addOnFailureListener { e -> cont.resume(Result.Error(e.toString()), null) }
            }
    }

}

