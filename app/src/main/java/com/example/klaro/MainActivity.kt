package com.example.klaro

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.klaro.domain.repository.implementations.ImportRepositoryImpl
import com.example.klaro.navigation.NavGraph
import com.example.klaro.ui.theme.KlaroTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            KlaroTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}

@HiltAndroidApp
class KlaroApplication : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        importSets();
    }

    /**
     * Metoda, która importuję fiszki do firebase
     * */
    private fun importSets()
    {
        val repo = ImportRepositoryImpl(FirebaseFirestore.getInstance(), this)
        repo.importPremiumSetFromAssets("PL-ENG-A1-GENERAL.xlsx")
        repo.importPremiumSetFromAssets("PL-DE-A1-GENERAL.xlsx")

    }
}


