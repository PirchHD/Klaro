package com.example.klaro.domain.repository.implementations

import com.example.klaro.domain.repository.Result
import com.example.klaro.domain.repository.interfaces.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl  @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : LoginRepository
{

    override suspend fun login(email: String, password: String): Result<FirebaseUser>
    {
        return try
        {
            val authResult = auth
                .signInWithEmailAndPassword(email.trim(), password)
                .await()
            Result.Success(authResult.user!!)
        }
        catch (e: Exception)
        {
            Result.Error(e.localizedMessage ?: "Nieznany błąd logowania")
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<FirebaseUser> = suspendCancellableCoroutine { cont ->
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user == null)
                {
                    cont.resume(Result.Error("Brak zalogowanego użytkownika"), null)
                    return@addOnSuccessListener
                }

                val isNew = result.additionalUserInfo?.isNewUser ?: false

                if (isNew)
                {
                    firestore.collection("users")
                        .document(user.uid)
                        .set(mapOf(
                            "firstName" to (user.displayName?.split(" ")?.firstOrNull() ?: ""),
                            "lastName"  to (user.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: ""),
                            "email"     to (user.email ?: ""),
                            "createdAt" to FieldValue.serverTimestamp()
                        ))
                        .addOnSuccessListener { cont.resume(Result.Success(user), null) }
                        .addOnFailureListener { e -> cont.resume(
                            Result.Error(
                                e.localizedMessage ?: "Błąd zapisu profilu"
                            ), null) }
                }
                else
                {
                    cont.resume(Result.Success(user), null)
                }
            }
            .addOnFailureListener { e ->
                cont.resume(Result.Error(e.localizedMessage ?: "Błąd logowania z Google"), null)
            }
    }

}
