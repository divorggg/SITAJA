package com.example.apptanjunglanjut.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val TealPastel = Color(0xFF1F7D53)
private val TealPastelLight = Color(0xFF255F38)
private val SoftGreen = Color(0xFFA3B087)
private val OnTeal = Color(0xFF27391C)



private val LightColors = lightColorScheme(
    primary = TealPastel,
    onPrimary = Color.White,
    secondary = TealPastelLight,
    surface = Color.White,
    onSurface = Color.Black,
    background = SoftGreen,
    onBackground = OnTeal
)


private val DarkColors = darkColorScheme(
    primary = TealPastel,
    onPrimary = Color.Black
)


@Composable
fun AppTanjunglanjutTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colors: ColorScheme = if (useDarkTheme) DarkColors else LightColors


    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}