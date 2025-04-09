package com.equipealpha.financaspessoais

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.equipealpha.financaspessoais.navigation.AppNavigation
import com.equipealpha.financaspessoais.ui.root.HomeRootScreen
import com.equipealpha.financaspessoais.ui.theme.FinancasPessoaisTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }

            FinancasPessoaisTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                HomeRootScreen(
                    navController = navController,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }

    }
}

