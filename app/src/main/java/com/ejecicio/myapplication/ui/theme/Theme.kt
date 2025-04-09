package com.ejecicio.myapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

//private val DarkColorScheme = darkColorScheme(
////    primary = Pink80,
////    secondary = PurpleGrey80,
////    tertiary = Pink80
//    primary = darkLightPink,
//    secondary = darkDarkPink,
//    tertiary = darkLightGray,
//    background = darkWhite,//en vd es BLACK
//    surface = darkDarkerGray,
//    onPrimary = darkBlack,//en vd es WHITE
//    onSecondary = darkWhite,//en vd es BLACK
//    onTertiary = darkBlack,//en vd es WHITE
//    onBackground = darkBlack,//en vd es WHITE
//    onSurface = darkWhite//en vd es BLACK
//)

//private val LightColorScheme = lightColorScheme(
////    primary = Purple40,
////    secondary = PurpleGrey40,
////    tertiary = Pink40
//    primary = lightPink,
//    secondary = darkPink,
//    tertiary = lightGray,
//    background = white,
//    surface = darkerGray,
//    onPrimary = black,
//    onSecondary = white,
//    onTertiary = black,
//    onBackground = black,
//    onSurface = black
private val LightColorScheme = lightColorScheme(
    primary = esnBlue,
    secondary = esnAccentBlue, // Replaced orange with accent blue
    tertiary = esnSurfaceLight,
    background = esnWhite,
    surface = esnLightGray,
    onPrimary = esnWhite,
    onSecondary = esnWhite,
    onTertiary = esnBlack,
    onBackground = esnBlack,
    onSurface = esnBlack
)

private val DarkColorScheme = darkColorScheme(
    primary = esnBlueDark,
    secondary = esnLightBlueDark, // Replaced orange dark with a light blue for dark mode
    tertiary = esnSurfaceDark,
    background = esnDarkGray,
    surface = esnSurfaceDark,
    onPrimary = esnTextLight,
    onSecondary = esnTextLight,
    onTertiary = esnTextLight,
    onBackground = esnTextLight,
    onSurface = esnTextLight
)


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */


//@Composable
//fun MyApplicationTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}


@Composable
fun MyApplicationTheme(
    dynamicColor: Boolean = true,
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme // Cambia el colorScheme basado en isDarkMode

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

