package com.equipealpha.financaspessoais.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(modifier = modifier) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination == item.route,
                onClick = { navController.navigate(item.route) },
                icon = { androidx.compose.material3.Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { androidx.compose.material3.Text(item.label) }
            )
        }
    }
}
