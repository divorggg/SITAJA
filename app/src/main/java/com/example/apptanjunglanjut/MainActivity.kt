package com.example.apptanjunglanjut

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import com.example.apptanjunglanjut.ui.theme.AppTanjunglanjutTheme


class MainActivity : ComponentActivity() {
    private val vm by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTanjunglanjutTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainApp(vm)
                }
            }
        }
    }
}
