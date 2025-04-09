package com.equipealpha.financaspessoais.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.equipealpha.financaspessoais.navigation.Routes

data class NavigationBarItemData(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    NavigationBarItemData(Routes.HOME, Icons.Default.Home, "Início"),
    NavigationBarItemData("transactions", Icons.Default.DateRange, "Transações"),
    NavigationBarItemData(Routes.SETTINGS, Icons.Default.Settings, "Config.")
)
