package com.equipealpha.financaspessoais.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf

val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("Nenhuma Activity foi fornecida")
}
