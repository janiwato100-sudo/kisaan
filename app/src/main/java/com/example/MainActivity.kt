package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.data.SolarDatabase
import com.example.data.SolarRepository
import com.example.ui.SolarAppUI
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SolarViewModel
import com.example.viewmodel.SolarViewModelFactory

class MainActivity : ComponentActivity() {
  private lateinit var database: SolarDatabase

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Build local Room persistent database
    database = Room.databaseBuilder(
        applicationContext,
        SolarDatabase::class.java,
        "solar_kisaan_db"
    ).fallbackToDestructiveMigration().build()

    val repository = SolarRepository(database.solarDao())
    val factory = SolarViewModelFactory(application, repository)

    setContent {
      MyApplicationTheme {
        val viewModel: SolarViewModel = viewModel(factory = factory)
        Surface(modifier = Modifier.fillMaxSize()) {
          SolarAppUI(viewModel)
        }
      }
    }
  }
}
